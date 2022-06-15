/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.lower

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext

class ReactiveIrFunctionVisitor(
    private val context: ReactivePluginContext
) : ReactiveAnnotationBasedExtension, IrElementVisitor<Unit, RcdFunction> {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        context.annotations

    override fun visitElement(element: IrElement, data : RcdFunction) {
        element.acceptChildren(this, data)
    }

    override fun visitCall(expression: IrCall, data : RcdFunction) {
        super.visitCall(expression, data)

        if (! isReactive(expression.symbol)) return

        data.slots += RcdSlot(expression.symbol, expression.startOffset, data.slots.size)
    }

}
