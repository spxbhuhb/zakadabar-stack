/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrExpression
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiBranch(
    val index : Int,
    val condition: IrExpression,
    val conditionDependencies : MutableList<Int>,
    val body : IrExpression,
    val bodyDependencies: MutableList<Int>
) : RuiElement {

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitBranch(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit
}