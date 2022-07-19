/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrLoop
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiLoop(
    ruiClass: RuiClass,
    index: Int,
    val irLoop: IrLoop
) : RuiBlock(ruiClass, index) {

    override fun transform() {
        TODO("Not yet implemented")
    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitLoop(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {

    }
}