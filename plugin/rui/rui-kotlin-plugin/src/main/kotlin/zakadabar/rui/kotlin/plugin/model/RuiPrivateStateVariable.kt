/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.declarations.IrVariable
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiPrivateStateVariable(
    override val ruiClass: RuiClass,
    override val index: Int,
    val irVariable : IrVariable,
) : RuiStateVariable {

    override val originalName = irVariable.name.identifier
    override val name = "originalName\$"

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitStateVariable(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit

}