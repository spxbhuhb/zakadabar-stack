/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassBuilder

/**
 * Finds the boundary between state definition and rendering parts of a RUI function.
 * Entry point is [findBoundary].
 */
class RuiRenderingVisitor(
    private val ruiContext: RuiPluginContext,
    private val builder: RuiClassBuilder
) : RuiAnnotationBasedExtension, IrElementVisitorVoid {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    fun buildFunctions() {

    }
}
