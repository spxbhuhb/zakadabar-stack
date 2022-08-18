/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.functions
import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols

interface RuiFragmentBuilder : RuiBuilder {

    val symbolMap: RuiClassSymbols
        get() = throw IllegalStateException()

    val propertyBuilder: RuiPropertyBuilder

    /**
     * Builds the fragment. Runs after all classes in the module fragment are
     * transformed into RuiClass. This ensures that references to other Rui
     * classes can be resolved properly.
     */
    fun build() {
        throw NotImplementedError()
    }

    /**
     * Fetch the instance of this fragment.
     */
    fun IrBlockBodyBuilder.irGetFragment(scope : IrSimpleFunction): IrExpression {
        // FIXME check receiver logic when having deeper structures
        return irGet(
            propertyBuilder.type,
            IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, scope.dispatchReceiverParameter !!.symbol),
            propertyBuilder.getter.symbol
        )
    }

    /**
     * Call a Rui function of this fragment.
     *
     * @param scope The function we call from,
     */
    fun irRuiCall(callee: IrSimpleFunction, scope: IrSimpleFunction, builder: IrBlockBodyBuilder) {
        builder.run {
            + irCallOp(
                callee.symbol,
                type = irBuiltIns.unitType,
                dispatchReceiver = irGetFragment(scope)
            )
        }
    }

    /**
     * Call a Rui function of this fragment.
     *
     * @param scope The function we call from,
     */
    fun irRuiCallWithBridge(callee: IrSimpleFunction, scope: IrSimpleFunction, builder: IrBlockBodyBuilder) {
        builder.run {
            + irCallOp(
                callee.symbol,
                type = irBuiltIns.unitType,
                dispatchReceiver = irGetFragment(scope),
                argument = irGet(scope.valueParameters.first())
            )
        }
    }

    /**
     * Call the external patch of this fragment. This is somewhat complex because the function
     * is stored in a variable. So, we have to:
     *
     * 1. fetch the fragment itself
     * 1. fetch the function from the variable in the fragment
     * 1. call the function with the fragment as dispatch receiver
     */
    fun irCallExternalPatch(function: IrSimpleFunction, builder: IrBlockBodyBuilder) {
        builder.run {

            val function1Type = irBuiltIns.functionN(1)
            val invoke = function1Type.functions.first { it.name.identifier == "invoke" }.symbol

            val fragment = irTemporary(irGetFragment(function))

            + irCallOp(
                invoke,
                type = irBuiltIns.unitType,
                dispatchReceiver = irCallOp(symbolMap.externalPatchGetter.symbol, function1Type.defaultType, irGet(fragment)),
                origin = IrStatementOrigin.INVOKE,
                argument = irGet(fragment)
            )
        }
    }

    fun IrBlockBuilder.irTraceGet(index: Int, receiver: IrExpression): IrExpression =
        symbolMap.getStateVariable(index).property.backingField
            ?.let { irGetField(receiver, it) }
            ?: irString("?")
}