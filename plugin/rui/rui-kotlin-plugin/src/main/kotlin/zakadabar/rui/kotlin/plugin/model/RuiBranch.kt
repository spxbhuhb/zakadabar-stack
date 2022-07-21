/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.expressions.IrBranch
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiBranch(
    val ruiClass: RuiClass,
    val index : Int,
    val irBranch: IrBranch,
    val condition : RuiExpression,
    val result: RuiExpression
) : RuiElement {

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitBranch(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        condition.accept(visitor, data)
        result.accept(visitor, data)
    }
}