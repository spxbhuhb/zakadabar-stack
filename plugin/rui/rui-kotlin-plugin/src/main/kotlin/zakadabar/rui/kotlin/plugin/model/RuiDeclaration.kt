/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiDeclaration(
    val ruiClass: RuiClass,
    val irDeclaration : IrDeclaration,
    val origin : RuiDeclarationOrigin,
    val dependencies : List<RuiStateVariable>
) : RuiElement {

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitDeclaration(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit

}