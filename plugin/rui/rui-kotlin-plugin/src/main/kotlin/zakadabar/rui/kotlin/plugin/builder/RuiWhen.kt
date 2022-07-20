/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrWhen
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiWhen(
    ruiClass: RuiClass,
    index: Int,
    val irWhen: IrWhen
) : RuiStatement(ruiClass, index) {

    val branches = mutableListOf<RuiBranch>()

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitWhen(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        branches.forEach { it.accept(visitor, data) }
    }
}