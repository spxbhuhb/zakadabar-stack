/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassBuilder

/**
 * Creates the RUI component class for each function annotated with Rui.
 */
class RuiFunctionVisitor(
    private val ruiContext: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitFunctionNew(declaration: IrFunction) : IrFunction {
        if ( ! declaration.isAnnotatedWithRui()) {
            return declaration
        }

        buildRuiClass(declaration)

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
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
