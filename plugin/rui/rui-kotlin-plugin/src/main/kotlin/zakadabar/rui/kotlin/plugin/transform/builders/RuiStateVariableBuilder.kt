/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import zakadabar.rui.kotlin.plugin.model.RuiExternalStateVariable
import zakadabar.rui.kotlin.plugin.model.RuiInternalStateVariable

class RuiStateVariableBuilder private constructor(
    ruiClassBuilder: RuiClassBuilder
) : RuiPropertyBuilder(ruiClassBuilder) {

    constructor(ruiClassBuilder: RuiClassBuilder, ruiStateVariable: RuiExternalStateVariable) : this(ruiClassBuilder) {
        name = ruiStateVariable.irValueParameter.name
        build(ruiStateVariable.irValueParameter)
    }

    constructor(ruiClassBuilder: RuiClassBuilder, ruiStateVariable: RuiInternalStateVariable) : this(ruiClassBuilder) {
        name = ruiStateVariable.irVariable.name
        build(ruiStateVariable.irVariable)
    }

    fun build(irValueParameter: IrValueParameter) {

        buildField(irValueParameter.type)
        buildProperty()

        val constructorParameter = ruiClassBuilder.constructor.addValueParameter {
            name = this@RuiStateVariableBuilder.name
            type = irValueParameter.type
            varargElementType = irValueParameter.varargElementType
        }

        field.initializer = irFactory
            .createExpressionBody(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                IrGetValueImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, constructorParameter.symbol, IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER
                )
            )
    }

    fun build(irVariable: IrVariable) {
        name = irVariable.name

        buildField(irVariable.type)
        buildProperty()

        irVariable.initializer?.let { initializer ->
            field.initializer = irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, initializer)
        }
    }

}