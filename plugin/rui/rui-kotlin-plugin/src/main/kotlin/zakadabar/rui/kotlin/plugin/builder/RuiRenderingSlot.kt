/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

/**
 * A slot in component rendering. A component slot is generated for each:
 *
 * - function call
 * - branch (`if` and `when`)
 * - loop (`for`, `while`, `do-while`)
 *
 * @property  index         Index of the slot, used to differentiate between slots.
 * @property  slotClass     The Rui component class that implements the slot component.
 * @property  name          Name of the component slot in the generated class.
 * @property  irProperty    The property that represents this component slot in the generated class.
 * @property  dependsOn     The state variable indices this slot depends on.
 * @property  create        The code block that creates this slot
 */
class RuiRenderingSlot private constructor(
    ruiClass: RuiClass,
    index: Int,
    baseName : String
) : RuiPropertyBase(ruiClass, index) {

    override val name = "$baseName\$cs$index"

    constructor(ruiClass: RuiClass, index: Int, irCall: IrCall) : this(ruiClass, index, irCall.symbol.owner.name.identifier) {
        buildField(irCall.type)
        buildProperty()
    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitRenderingSlot(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit
}