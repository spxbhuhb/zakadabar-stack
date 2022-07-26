/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.expressions.IrBlock
import zakadabar.rui.kotlin.plugin.transform.RUI_BLOCK
import zakadabar.rui.kotlin.plugin.transform.builders.RuiBuilder
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiBlock(
    ruiClass: RuiClass,
    index : Int,
    val irBlock : IrBlock
) : RuiStatement(ruiClass, index) {

    override val name = "$RUI_BLOCK$index"

    val statements = mutableListOf<RuiStatement>()

    override val builder: RuiBuilder
        get() = TODO("Not yet implemented")

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitBlock(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        statements.forEach { it.accept(visitor, data) }
    }
}