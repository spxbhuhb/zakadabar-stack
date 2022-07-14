/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassBuilder

/**
 * Creates the RUI component class for each function annotated with Rui.
 */
class RuiFunctionVisitor(
    private val ruiContext: RuiPluginContext
) : RuiAnnotationBasedExtension, IrElementVisitorVoid {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    override fun visitFunction(declaration: IrFunction) {
        if (declaration.isAnnotatedWithRui()) {
            buildRuiClass(declaration)
        }
        super.visitFunction(declaration)
    }

    private fun buildRuiClass(declaration: IrFunction) {
        RuiClassBuilder(
            ruiContext,
            declaration,
            RuiBoundaryVisitor(ruiContext).findBoundary(declaration)
        ).build {
            if (boundary != 0) {
                RuiStateDefinitionTransformer(ruiContext, this).buildStateDefinition()
            }
            if (boundary != -1) {
                RuiRenderingVisitor(ruiContext, this).buildFunctions()
            }
        }.also {
            println("==== GENERATED CLASS: ${it.fqNameWhenAvailable} ====")
            println(it.dump())
        }


    }

}
