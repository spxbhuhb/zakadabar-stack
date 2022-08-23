/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.expressions.IrCall
import zakadabar.rui.kotlin.plugin.RUI_CALL
import zakadabar.rui.kotlin.plugin.toRuiClassFqName
import zakadabar.rui.kotlin.plugin.transform.builders.RuiCallBuilder
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiCall(
    ruiClass: RuiClass,
    index: Int,
    val irCall: IrCall
) : RuiStatement(ruiClass, index) {

    override val name = "$RUI_CALL$index"

    val targetRuiClass = irCall.symbol.owner.toRuiClassFqName(ruiClass.ruiContext)

    val valueArguments = mutableListOf<RuiExpression>()

    override val builder = RuiCallBuilder(ruiClass.builder, this)

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitCall(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        valueArguments.forEach { it.accept(visitor, data) }
    }
}