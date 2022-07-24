/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.deepCopyWithVariables
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.model.RuiCall
import zakadabar.rui.kotlin.plugin.model.RuiExpression
import zakadabar.rui.kotlin.plugin.transform.RUI_CLASS_RUI_ARGUMENTS
import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException

class RuiCallBuilder(
    override val ruiClassBuilder: RuiClassBuilder,
    val ruiCall: RuiCall
) : RuiFragmentBuilder {

    // we have to initialize this in build, after all other classes in the module are registered
    override lateinit var symbolMap: RuiClassSymbols

    override lateinit var propertyBuilder : RuiPropertyBuilder

    override fun build() {
        withSymbolMap {
            propertyBuilder = RuiPropertyBuilder(ruiClassBuilder, Name.identifier(ruiCall.name), symbolMap.defaultType, false)
            buildInitializer()
        }
    }

    fun withSymbolMap(func: () -> Unit) {
        try {
            symbolMap = ruiContext.ruiSymbolMap.getSymbolMap(ruiCall.targetRuiClass)
            func()
        } catch (ex: RuiCompilationException) {
            ex.error.report(ruiClassBuilder, ruiCall.irCall)
            return
        }
    }

    fun buildInitializer() {
        irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET) {
            this.expression = IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                symbolMap.defaultType,
                symbolMap.primaryConstructor.symbol,
                0, 0,
                ruiCall.valueArguments.size + RUI_CLASS_RUI_ARGUMENTS // +2 = adapter + anchor
            ).also { constructorCall ->

                constructorCall.putValueArgument(0, irGet(ruiClassBuilder.adapter))
                constructorCall.putValueArgument(1, irGet(ruiClassBuilder.anchor))
                constructorCall.putValueArgument(2, buildPatchStateVariable())

                ruiCall.valueArguments.forEachIndexed { index, ruiExpression ->
                    constructorCall.putValueArgument(index + RUI_CLASS_RUI_ARGUMENTS, ruiExpression.irExpression)
                }
            }
        }.also {
            propertyBuilder.irField.initializer = it
        }
    }

    fun buildPatchStateVariable(): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = irBuiltIns.unitType
        }.also { function ->

            function.parent = propertyBuilder.irField

            val patchStateIt = function.addValueParameter {
                name = Name.identifier("it")
                type = ruiContext.ruiFragmentType
            }

            function.body = buildPatchStateBody(function, patchStateIt)
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            ruiContext.ruiPatchStateType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }

    private fun buildPatchStateBody(function: IrSimpleFunction, patchStateIt: IrValueParameter): IrBody? {
        return try {

            DeclarationIrBuilder(irContext, function.symbol).irBlockBody {

                + irAs(symbolMap.defaultType, irGet(patchStateIt))

                ruiCall.valueArguments.mapIndexedNotNull { index, ruiExpression ->
                    buildPatchStateVariable(patchStateIt, index, ruiExpression)
                }.forEach { + it }
            }

        } catch (ex: RuiCompilationException) {
            ex.error.report(ruiClassBuilder, ruiCall.irCall)
            DeclarationIrBuilder(irContext, function.symbol).irBlockBody { }
        }
    }


    fun buildPatchStateVariable(patchStateIt: IrValueDeclaration, index: Int, ruiExpression: RuiExpression): IrExpression? {
        // constants, globals, etc. have no dependencies, no need to patch them
        if (ruiExpression.dependencies.isEmpty()) return null

        return irIf(
            buildCondition(ruiExpression),
            buildPatchResult(patchStateIt, index, ruiExpression)
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

    private fun buildPatchResult(patchStateIt: IrValueDeclaration, index: Int, ruiExpression: RuiExpression): IrExpression {
        return irBlock(
            statements = listOf(
                irCall(
                    symbolMap.setterFor(index), null,
                    irImplicitAs(symbolMap.defaultType, irGet(patchStateIt)), null,
                    ruiExpression.irExpression.deepCopyWithVariables()
                ),
                irCall(
                    symbolMap.getInvalidate(index / 32), null,
                    irImplicitAs(symbolMap.defaultType, irGet(patchStateIt)), null,
                    irConst(1 shl (index % 32))
                )
            )
        )
    }
}