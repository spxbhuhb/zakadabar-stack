/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import zakadabar.rui.kotlin.plugin.rendering.RuiDependencyVisitor
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiWhen(
    ruiClass: RuiClass,
    index: Int,
    val irWhen: IrWhen
) : RuiBlock(ruiClass, index) {

    val branches = mutableListOf<RuiBranch>()

    override fun transform() {
        irWhen.branches.forEach { irBranch ->
            branches += RuiBranch(
                index = 0,
                irBranch.condition,
                RuiDependencyVisitor(ruiClass).apply { irBranch.condition.acceptVoid(this) }.dependencies,
                irBranch.result,
                RuiDependencyVisitor(ruiClass).apply { irBranch.result.acceptVoid(this) }.dependencies,
            )
        }

    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitWhen(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        branches.forEach { it.accept(visitor, data) }
    }
}