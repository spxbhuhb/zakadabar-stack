/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrLoop
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiLoopBlock(
    ruiClass: RuiClass,
    name: String,
    val irLoop: IrLoop
) : RuiBlock(ruiClass, name) {

    override fun transform() {
        TODO("Not yet implemented")
    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitLoopBlock(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {

    }
}