/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addDispatchReceiver
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.model.RuiCall
import zakadabar.rui.kotlin.plugin.model.RuiExpression
import zakadabar.rui.kotlin.plugin.transform.RUI_CLASS_RUI_ARGUMENTS
import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols
import zakadabar.rui.kotlin.plugin.transform.toir.RuiReceiverTransform
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException

class RuiCallBuilder(
    override val ruiClassBuilder: RuiClassBuilder,
    val ruiCall: RuiCall
) : RuiBuilder {

    // we have to initialize this in build, after all other classes in the module are registered
    lateinit var symbolMap: RuiClassSymbols

    override fun build() {
        withSymbolMap {
            buildFragment()
            buildPatch()
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

    val fragmentProperty = RuiPropertyBuilder(ruiClassBuilder, Name.identifier(ruiCall.name), ruiContext.ruiFragmentType, false)

    fun buildFragment() {
        irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET) {
            this.expression = IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                symbolMap.defaultType,
                symbolMap.primaryConstructor.symbol,
                0, 0,
                ruiCall.valueArguments.size + RUI_CLASS_RUI_ARGUMENTS // +2 = adapter + anchor
            ).also { constructorCall ->
                constructorCall.putValueArgument(0, ruiClassBuilder.adapterProperty.irGet())
                constructorCall.putValueArgument(1, ruiClassBuilder.anchorProperty.irGet())
                ruiCall.valueArguments.forEachIndexed { index, ruiExpression ->
                    constructorCall.putValueArgument(index + RUI_CLASS_RUI_ARGUMENTS, ruiExpression.irExpression)
                }
            }
        }.also {
            fragmentProperty.irField.initializer = it
        }
    }

    fun buildPatch() {
        irFactory.buildFun {
            name = Name.identifier("${ruiCall.name}Patch")
            returnType = irBuiltIns.unitType
        }.also { function ->

            function.parent = irClass

            function.addDispatchReceiver {
                type = irClass.defaultType
            }

            function.body = try {

                DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
                    ruiCall.valueArguments.mapIndexedNotNull { index, ruiExpression ->
                        buildPatch(function, index, ruiExpression)
                    }.forEach { + it }
                    + irCallPatch(function)
                }

            } catch (ex: RuiCompilationException) {
                ex.error.report(ruiClassBuilder, ruiCall.irCall)
                DeclarationIrBuilder(irContext, function.symbol).irBlockBody { }
            }

            ruiClassBuilder.irClass.declarations += function
        }
    }

    fun IrSimpleFunction.irGetReceiver() =
        irGet(irClass.defaultType, this.dispatchReceiverParameter!!.symbol)

    fun buildPatch(function : IrSimpleFunction, index: Int, ruiExpression: RuiExpression): IrExpression? {
        // constants, globals, etc. have no dependencies, no need to patch them
        if (ruiExpression.dependencies.isEmpty()) return null

        return irIf(
            buildCondition(function, ruiExpression),
            buildPatchResult(function, index, ruiExpression)
        )
    }

    fun buildCondition(function : IrSimpleFunction, ruiExpression: RuiExpression): IrExpression {
        val dependencies = ruiExpression.dependencies
        var result = dependencies[0].builder.irIsDirty(function.irGetReceiver())
        for (i in 1 until dependencies.size) {
            result = irOrOr(result, dependencies[i].builder.irIsDirty(function.irGetReceiver()))
        }
        return result
    }

    private fun buildPatchResult(function : IrSimpleFunction, index: Int, ruiExpression: RuiExpression): IrExpression {
        return irBlock(
            statements = listOf(
                irCall(
                    symbolMap.setterFor(index), null,
                    fragmentProperty.irGetValue(function.irGetReceiver()), null,
                    RuiReceiverTransform(ruiClassBuilder, irClass, function).copyAndTransform(ruiExpression.irExpression)
                ),
                irCall(
                    symbolMap.getInvalidate(index / 32), null,
                    fragmentProperty.irGetValue(function.irGetReceiver()), null,
                    irConst(1 shl (index % 32))
                )
            )
        )
    }

    fun irCallPatch(function : IrSimpleFunction): IrExpression =
        irCall(
            irBuiltIns.functionN(0).defaultType.unaryOperator(Name.identifier("invoke")),
            dispatchReceiver = irCall(
                symbolMap.patch,
                dispatchReceiver = fragmentProperty.irGetValue(function.irGetReceiver())
            )
        )
}