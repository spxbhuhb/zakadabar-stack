/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.deepCopyWithVariables
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.RUI_FRAGMENT_ARGUMENT_COUNT
import zakadabar.rui.kotlin.plugin.RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER
import zakadabar.rui.kotlin.plugin.RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH
import zakadabar.rui.kotlin.plugin.RUI_FRAGMENT_TYPE_INDEX_BRIDGE
import zakadabar.rui.kotlin.plugin.model.RuiCall
import zakadabar.rui.kotlin.plugin.model.RuiExpression
import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException

class RuiCallBuilder(
    override val ruiClassBuilder: RuiClassBuilder,
    val ruiCall: RuiCall
) : RuiFragmentBuilder {

    // we have to initialize this in build, after all other classes in the module are registered
    override lateinit var symbolMap: RuiClassSymbols

    override fun irNewInstance(): IrExpression =
        tryBuild(ruiCall.irCall) {
            symbolMap = ruiContext.ruiSymbolMap.getSymbolMap(ruiCall.targetRuiClass)

            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                symbolMap.defaultType,
                symbolMap.primaryConstructor.symbol,
                typeArgumentsCount = 1, // bridge type
                constructorTypeArgumentsCount = 0,
                ruiCall.valueArguments.size + RUI_FRAGMENT_ARGUMENT_COUNT // +2 = adapter + external patch
            ).also { constructorCall ->

                constructorCall.putTypeArgument(RUI_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)
                constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER, ruiClassBuilder.adapterPropertyBuilder.irGetValue())
                constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH, buildExternalPatch())

                ruiCall.valueArguments.forEachIndexed { index, ruiExpression ->
                    constructorCall.putValueArgument(index + RUI_FRAGMENT_ARGUMENT_COUNT, ruiExpression.irExpression)
                }
            }
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
                type = classBoundFragmentType
            }

            function.body = buildExternalPatchBody(function, externalPatchIt)
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundExternalPatchType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }

    private fun buildExternalPatchBody(function: IrSimpleFunction, externalPatchIt: IrValueParameter): IrBody? {
        return try {

            DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
                traceExternalPatch()

                + irAs(symbolMap.defaultType, irGet(externalPatchIt))

                ruiCall.valueArguments.forEachIndexed { index, ruiExpression ->
                    buildVariablePatch(externalPatchIt, index, ruiExpression)
                }
            }

        } catch (ex: RuiCompilationException) {
            ex.error.report(ruiClassBuilder, ruiCall.irCall, additionalInfo = ex.additionalInfo)
            DeclarationIrBuilder(irContext, function.symbol).irBlockBody { }
        }
    }

    fun IrBlockBodyBuilder.traceExternalPatch() {

        if (! ruiContext.withTrace) return

        val args = mutableListOf<IrExpression>()

        ruiClass.dirtyMasks.forEach {
            args += irString("${it.name}:")
            args += it.builder.propertyBuilder.irGetValue(ruiClass.builder.irThisReceiver())
        }

        ruiClassBuilder.irTrace(ruiClass.builder.irThisReceiver(), "external patch", args)

    }

    fun IrBlockBodyBuilder.buildVariablePatch(externalPatchIt: IrValueDeclaration, index: Int, ruiExpression: RuiExpression) {
        // constants, globals, etc. have no dependencies, no need to patch them
        if (ruiExpression.dependencies.isEmpty()) return

        // lambdas and anonymous functions cannot change and cannot be patched
        // FIXME check if lambda and anonymous func results in IrFunctionExpression
        if (ruiExpression.irExpression is IrFunctionExpression) return

        + irIf(
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
        return irBlock {
            val traceData = traceStateChangeBefore(externalPatchIt, index)

            // TODO decide if this irTemporary has a real undesired effect (it is here solely because of trace)
            // TODO check what deepCopyWithVariables exactly does
            val newValue = irTemporary(ruiExpression.irExpression.deepCopyWithVariables())

            // set the state variable in the child fragment
            + irCall(
                symbolMap.setterFor(index),
                origin = null,
                dispatchReceiver = irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt)),
                extensionReceiver = null,
                irGet(newValue)
            )

            // call invalidate of the child fragment
            + irCall(
                symbolMap.getInvalidate(index / 32), null,
                irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt)), null,
                irConst(1 shl (index % 32))
            )

            traceStateChangeAfter(index, traceData, newValue)
        }
    }

    fun IrBlockBuilder.traceStateChangeBefore(externalPatchIt: IrValueDeclaration, index: Int): IrVariable? {

        if (! ruiContext.withTrace) return null

        return irTemporary(irTraceGet(index, irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt))))
    }

    fun IrBlockBuilder.traceStateChangeAfter(index: Int, traceData: IrVariable?, newValue: IrVariable) {
        if (traceData == null) return

        val property = symbolMap.getStateVariable(index).property

        ruiClassBuilder.irTrace("state change", listOf(
            irString("${property.name}:"),
            irGet(traceData),
            irString(" ⇢ "),
            irGet(newValue)
        ))
    }

}