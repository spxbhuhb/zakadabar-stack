/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.expressions.IrBlock
import zakadabar.rui.kotlin.plugin.RUI_FOR_LOOP
import zakadabar.rui.kotlin.plugin.transform.builders.RuiFragmentBuilder
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiForLoop(
    ruiClass: RuiClass,
    index: Int,
    val irBlock: IrBlock,
    var iterator: RuiDeclaration,
    val condition: RuiExpression,
    val loopVariable: RuiDeclaration,
    val body: RuiStatement,
) : RuiStatement(ruiClass, index) {

    override val name = "$RUI_FOR_LOOP$index"

    override val builder: RuiFragmentBuilder
        get() = TODO("Not yet implemented")

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitForLoop(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        iterator.accept(visitor, data)
        condition.accept(visitor, data)
        loopVariable.accept(visitor, data)
        body.accept(visitor, data)
    }
}