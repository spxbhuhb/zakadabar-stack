/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.model.RuiExternalStateVariable
import zakadabar.rui.kotlin.plugin.model.RuiInternalStateVariable
import zakadabar.rui.kotlin.plugin.model.RuiStateVariable

class RuiStateVariableBuilder private constructor(
    ruiClassBuilder: RuiClassBuilder,
    val ruiStateVariable: RuiStateVariable,
    name: Name,
    type: IrType
) : RuiPropertyBuilder(ruiClassBuilder, name, type) {

    companion object {
        fun builderFor(ruiStateVariable: RuiExternalStateVariable) =
            RuiStateVariableBuilder(
                ruiStateVariable.ruiClass.builder,
                ruiStateVariable,
                ruiStateVariable.name,
                ruiStateVariable.irValueParameter.type
            ).apply {
                initExternal(ruiStateVariable)
            }


        fun builderFor(ruiStateVariable: RuiInternalStateVariable) =
            RuiStateVariableBuilder(
                ruiStateVariable.ruiClass.builder,
                ruiStateVariable,
                ruiStateVariable.name,
                ruiStateVariable.irVariable.type
            ).apply {
                initInternal(ruiStateVariable)
            }
    }

    fun initExternal(ruiStateVariable: RuiExternalStateVariable) {

        val constructorParameter = ruiClassBuilder.constructor.addValueParameter {
            name = this@RuiStateVariableBuilder.name
            type = ruiStateVariable.irValueParameter.type
            varargElementType = ruiStateVariable.irValueParameter.varargElementType
        }

        irField.initializer = irFactory
            .createExpressionBody(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                IrGetValueImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, constructorParameter.symbol, IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER
                )
            )

    }

    fun initInternal(ruiStateVariable: RuiInternalStateVariable) {

        ruiStateVariable.irVariable.initializer?.let { initializer ->
            irField.initializer = irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, initializer)
        }

    }

    fun irIsDirty(receiver : IrExpression) : IrExpression {
        val variableIndex = ruiStateVariable.index
        val maskIndex = variableIndex / 32
        val bitIndex = variableIndex % 32

        val mask = ruiClassBuilder.ruiClass.dirtyMasks[maskIndex]

        return irNotEqual(
            irAnd(mask.builder.irGetValue(receiver), irConst(bitIndex)),
            irConst(0)
        )
    }

}