/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.deepCopyWithVariables
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.model.RuiCall
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.model.RuiExpression
import zakadabar.rui.kotlin.plugin.transform.RUI_FRAGMENT_ARGUMENT_COUNT
import zakadabar.rui.kotlin.plugin.transform.RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER
import zakadabar.rui.kotlin.plugin.transform.RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH
import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException
import zakadabar.rui.kotlin.plugin.util.traceLabel

class RuiCallBuilder(
    override val ruiClass: RuiClass,
    val ruiCall: RuiCall
) : RuiFragmentBuilder {

    // we have to initialize this in build, after all other classes in the module are registered
    override lateinit var symbolMap: RuiClassSymbols

    override lateinit var propertyBuilder: RuiPropertyBuilder

    override fun build() {
        symbolMap = ruiContext.ruiSymbolMap.getSymbolMap(ruiCall.targetRuiClass)

        if (! symbolMap.valid) {
            ErrorsRui.RUI_IR_INVALID_EXTERNAL_CLASS.report(ruiClass, ruiCall.irCall, additionalInfo = "invalid symbol map for ${ruiCall.targetRuiClass}")
            return
        }

        propertyBuilder = RuiPropertyBuilder(ruiClass, Name.identifier(ruiCall.name), symbolMap.defaultType, isVar = false)
        buildInitializer()
    }

    fun buildInitializer() {
        if (! symbolMap.valid) return

        val value = IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            symbolMap.defaultType,
            symbolMap.primaryConstructor.symbol,
            0, 0,
            ruiCall.valueArguments.size + RUI_FRAGMENT_ARGUMENT_COUNT // +2 = adapter + external patch
        ).also { constructorCall ->

            constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER, irGet(ruiClassBuilder.adapter))
            constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH, buildExternalPatch())

            ruiCall.valueArguments.forEachIndexed { index, ruiExpression ->
                constructorCall.putValueArgument(index + RUI_FRAGMENT_ARGUMENT_COUNT, ruiExpression.irExpression)
            }
        }

        ruiClassBuilder.initializer.body.statements += IrSetFieldImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            propertyBuilder.irField.symbol,
            receiver = ruiClassBuilder.irThisReceiver(),
            value = value,
            irBuiltIns.unitType
        )
    }

    fun buildExternalPatch(): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = irBuiltIns.unitType
        }.also { function ->

            function.parent = irClass
            function.visibility = DescriptorVisibilities.LOCAL

            val externalPatchIt = function.addValueParameter {
                name = Name.identifier("it")
                type = ruiContext.ruiFragmentType
            }

            function.body = buildExternalPatchBody(function, externalPatchIt)
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            ruiContext.ruiExternalPatchType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }

    private fun buildExternalPatchBody(function: IrSimpleFunction, externalPatchIt: IrValueParameter): IrBody? {
        return try {

            DeclarationIrBuilder(irContext, function.symbol).irBlockBody {

                if (ruiContext.withTrace) {
                    traceExternalPatch()
                }

                + irAs(symbolMap.defaultType, irGet(externalPatchIt))

                ruiCall.valueArguments.mapIndexedNotNull { index, ruiExpression ->
                    buildVariablePatch(externalPatchIt, index, ruiExpression)
                }.forEach { + it }
            }

        } catch (ex: RuiCompilationException) {
            ex.error.report(ruiClassBuilder, ruiCall.irCall, additionalInfo = ex.additionalInfo)
            DeclarationIrBuilder(irContext, function.symbol).irBlockBody { }
        }
    }

    fun IrBlockBodyBuilder.traceExternalPatch() {

        val concat = irConcat()
        concat.addArgument(irString(traceLabel(ruiCall.name, "external patch")))

        ruiClass.dirtyMasks.forEach {
            concat.addArgument(irString(" ${ruiClass.name}.${it.name}: "))
            concat.addArgument(it.builder.propertyBuilder.irGetValue(ruiClass.builder.irThisReceiver()))
        }

        + ruiClass.builder.irPrintln(concat)
    }

    fun IrBlockBodyBuilder.buildVariablePatch(externalPatchIt: IrValueDeclaration, index: Int, ruiExpression: RuiExpression): IrExpression? {
        // constants, globals, etc. have no dependencies, no need to patch them
        if (ruiExpression.dependencies.isEmpty()) return null

        return irIf(
            buildCondition(ruiExpression),
            buildPatchResult(externalPatchIt, index, ruiExpression)
        )
    }

    fun buildCondition(ruiExpression: RuiExpression): IrExpression {
        val dependencies = ruiExpression.dependencies
        var result = dependencies[0].builder.irIsDirty(irThisReceiver())
        for (i in 1 until dependencies.size) {
            result = irOrOr(result, dependencies[i].builder.irIsDirty(irThisReceiver()))
        }
        return result
    }

    private fun IrBlockBodyBuilder.buildPatchResult(externalPatchIt: IrValueDeclaration, index: Int, ruiExpression: RuiExpression): IrExpression {
        val statements = mutableListOf<IrStatement>()

        val value = if (ruiContext.withTrace) {
            traceStateChange(externalPatchIt, ruiExpression.irExpression.deepCopyWithVariables(), index)  // TODO check what deepCopyWithVariables exactly does
        } else {
            ruiExpression.irExpression.deepCopyWithVariables() // TODO check what deepCopyWithVariables exactly does
        }

        // set the state variable in the child fragment
        statements += irCall(
            symbolMap.setterFor(index), null,
            irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt)), null,
            value
        )

        // call invalidate of the child fragment
        statements += irCall(
            symbolMap.getInvalidate(index / 32), null,
            irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt)), null,
            irConst(1 shl (index % 32))
        )

        return irBlock(statements = statements)
    }

    fun IrBlockBodyBuilder.traceStateChange(externalPatchIt: IrValueDeclaration, expression : IrExpression, index : Int) : IrExpression {
        val stateVariable = symbolMap.getStateVariable(index).property.name

        val oldValue = irCall(
            symbolMap.getterFor(index), null,
            irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt))
        )

        val newValue = irTemporary(expression)

        val concat = irConcat()
        concat.addArgument(irString(traceLabel(ruiCall.name, "state change")))
        concat.addArgument(irString(" $stateVariable: "))
        concat.addArgument(oldValue)
        concat.addArgument(irString(" ⇢ "))
        concat.addArgument(irGet(newValue))

        + ruiClass.builder.irPrintln(concat)

        return irGet(newValue)
    }

}