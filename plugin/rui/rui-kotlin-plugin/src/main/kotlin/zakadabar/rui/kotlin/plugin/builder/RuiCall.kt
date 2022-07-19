/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import zakadabar.rui.kotlin.plugin.rendering.RuiDependencyVisitor
import zakadabar.rui.kotlin.plugin.state.definition.toRuiClassFqName
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiCall(
    ruiClass: RuiClass,
    index: Int,
    val irCall: IrCall
) : RuiBlock(ruiClass, index) {

    val targetRuiClass = irCall.symbol.owner.toRuiClassFqName()
    val callParameters = mutableListOf<RuiCallParameter>()

    override fun transform() {
        for (i in 0 until irCall.valueArgumentsCount) {
            val expression = irCall.getValueArgument(i)!!
            val visitor = RuiDependencyVisitor(ruiClass).apply { expression.acceptVoid(this) }

            callParameters += RuiCallParameter(i, expression, visitor.dependencies)
        }
    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitCall(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        callParameters.forEach { it.accept(visitor, data) }
    }
}