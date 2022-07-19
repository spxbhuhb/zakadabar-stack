/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

/**
 * Variable in the component state. A state variable is generated for each:
 *
 * - parameter of the original function
 * - top level variable declared in the original function
 *
 * @property  index         Index of the bit in `dirty` that belongs to this state variable.
 * @property  originalName  The name of the variable in the original function.
 * @property  name          Name of the state variable in the generated class.
 * @property  irProperty    The property that represents this state variable in the generated class.
 * @property  getter        The getter function of [irProperty].
 * @property  setter        The setter function of [irProperty].
 */
class RuiStateVariable private constructor(
    ruiClass: RuiClass,
    index: Int,
    val originalName: String,
    override val name: String
) : RuiPropertyBase(ruiClass, index) {

    constructor(ruiClass: RuiClass, index: Int, irVariable: IrVariable) : this(ruiClass, index, irVariable.name.identifier, "${irVariable.name}\$") {
        buildField(irVariable.type)
        buildProperty()

        irVariable.initializer?.let { initializer ->
            field.initializer = ruiClass.irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, initializer)
        }
    }

    constructor(ruiClass: RuiClass, index: Int, irValueParameter: IrValueParameter) : this(ruiClass, index, irValueParameter.name.identifier, irValueParameter.name.identifier) {

        buildField(irValueParameter.type)
        buildProperty()

        field.initializer = ruiClass.irFactory
            .createExpressionBody(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                IrGetValueImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irValueParameter.symbol, IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER
                )
            )
    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitStateVariable(this, data)
    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit

}