/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.ir.addChild
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassCompilation

/**
 * Calls the appropriate transformer to:
 *
 * 1. create the Rui component class for the function
 * 2. transform the rendering into state management functions of the class
 *
 * The visitor is called twice. This separating is here, so rendering transformation
 * already has all the classes available.
 */
class RuiFunctionVisitor(
    private val ruiContext: RuiPluginContext,
    private val phase: RuiCompilationPhase
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitFileNew(declaration: IrFile): IrFile {
        val result = super.visitFileNew(declaration)

        if (phase == RuiCompilationPhase.StateDefinition) {
            result.declarations
                .filter { it is IrFunction && it.isAnnotatedWithRui() }
                .forEach {
                    result.addChild(ruiContext.ruiClassFor(it as IrFunction))
                }
        }

        declaration.metadata

        return result
    }

    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (! declaration.isAnnotatedWithRui()) {
            return declaration
        }

        when (phase) {
            RuiCompilationPhase.StateDefinition -> stateDefinition(declaration)
            RuiCompilationPhase.Rendering -> rendering(declaration)
        }

        return declaration
    }

    private fun stateDefinition(declaration: IrFunction) {

        RuiClassCompilation(
            ruiContext,
            declaration,
            RuiBoundaryVisitor(ruiContext).findBoundary(declaration)
        ).build {

            RuiStateDefinitionTransformer(ruiContext, this).buildStateDefinition()

        }

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

    }

    private fun rendering(declaration: IrFunction) {

        val builder = ruiContext.classBuilders[declaration.name.toRuiClassName().asString()]
            ?: throw IllegalStateException("builder is missing for ${declaration.name.toRuiClassName()}")

        RuiRenderingTransformer(ruiContext, builder).buildRendering()
    }


}
