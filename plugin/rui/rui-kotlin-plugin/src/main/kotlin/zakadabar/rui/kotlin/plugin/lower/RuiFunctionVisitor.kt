/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext

/**
 * Creates the RUI component class from a function annotated with [RuiComponent].
 */
class RuiFunctionVisitor(
    private val context: IrPluginContext,
    private val configuration: RuiPluginContext
) : RuiAnnotationBasedExtension, IrElementVisitorVoid {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        configuration.annotations

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    override fun visitFunction(declaration: IrFunction) {
        if (declaration.isAnnotatedWithRui()) {
            RuiBoundaryVisitor(context, configuration).findBoundary(declaration).also { renderStart ->
                println("==== ${declaration.symbol} BOUNDARY ====")
                println("    renderStart: $renderStart")
            }

        }
        super.visitFunction(declaration)
    }

}
