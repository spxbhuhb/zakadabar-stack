/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.expressions.IrCall
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor
import zakadabar.rui.kotlin.plugin.util.toRuiClassFqName

class RuiCall(
    ruiClass: RuiClass,
    index: Int,
    val irCall: IrCall
) : RuiStatement(ruiClass, index) {

    val targetRuiClass = irCall.symbol.owner.toRuiClassFqName()
    val valueArguments = mutableListOf<RuiExpression>()

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitCall(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        valueArguments.forEach { it.accept(visitor, data) }
    }
}