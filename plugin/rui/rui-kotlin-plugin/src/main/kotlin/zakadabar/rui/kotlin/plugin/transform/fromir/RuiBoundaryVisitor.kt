/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.fromir

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

/**
 * Finds the boundary between state definition and rendering parts of a RUI function.
 * Entry point is [findBoundary].
 */
class RuiBoundaryVisitor(
    private val ruiContext: RuiPluginContext
) : RuiAnnotationBasedExtension, IrElementVisitorVoid {

    var found: Boolean = false

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    /**
     * Finds the boundary between state definition and rendering parts of a RUI function.
     *
     * @return  Index of the first rendering statement in the function's
     *          statement list or the size of the list if there is no rendering part.
     */
    fun findBoundary(declaration: IrFunction): Int {

        declaration.body?.statements?.let { statements ->

            statements.forEachIndexed { index, irStatement ->
                irStatement.acceptVoid(this)
                if (found) return index
            }

            return statements.size
        }

        throw IllegalStateException("function has no body (maybe it's an expression function)")
    }

    override fun visitElement(element: IrElement) {
        if (found) return
        element.acceptChildren(this, null)
    }

    override fun visitCall(expression: IrCall) {
        if (expression.symbol.owner.isAnnotatedWithRui()) {
            found = true
        } else {
            super.visitCall(expression)
        }
    }
}
