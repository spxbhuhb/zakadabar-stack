/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.ir.expressions.IrCall
import zakadabar.rui.kotlin.plugin.rendering.RuiDependencyVisitor
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiCallBlock(
    ruiClass: RuiClass,
    name: String,
    val irCall: IrCall
) : RuiBlock(ruiClass, name) {

    val callParameters = mutableListOf<RuiCallParameter>()

    override fun transform() {
        for (i in 0 until irCall.valueArgumentsCount) {
            callParameters += RuiCallParameter(
                index = i,
                irExpression = irCall.getValueArgument(i)!!,
                dependencies = RuiDependencyVisitor(ruiClass).dependencies
            )
        }
    }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitCallBlock(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        callParameters.forEach { it.accept(visitor, data) }
    }
}