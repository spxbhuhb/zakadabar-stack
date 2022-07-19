/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrWhen
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiBranchBlock(
    ruiClass: RuiClass,
    name : String,
    val irWhen : IrWhen
) : RuiBlock(ruiClass, name) {

    override fun transform() {
        TODO("Not yet implemented")
    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitBranchBlock(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {

    }
}