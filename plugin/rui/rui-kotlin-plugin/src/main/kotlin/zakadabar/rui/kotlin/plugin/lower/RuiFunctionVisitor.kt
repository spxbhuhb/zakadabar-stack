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
import zakadabar.rui.kotlin.plugin.builder.RuiClass
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_FUNCTION_BODY

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

    val generatedClasses = mutableListOf<RuiClass>()

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitFileNew(declaration: IrFile): IrFile {
        generatedClasses.clear()

        val result = super.visitFileNew(declaration)

        generatedClasses.forEach {
            it.finalize()
            result.addChild(it.irClass)
        }

        return result
    }

    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (! declaration.isAnnotatedWithRui()) {
            return declaration
        }

        if (declaration.body == null) {
            if (phase == RuiCompilationPhase.StateDefinition) {
                RUI_IR_MISSING_FUNCTION_BODY.report(ruiContext, declaration)
            }
            return declaration
        }

        when (phase) {
            RuiCompilationPhase.StateDefinition -> stateDefinition(declaration)
            RuiCompilationPhase.Rendering -> rendering(declaration)
        }

        return declaration
    }

    private fun stateDefinition(declaration: IrFunction) {

        RuiClass(ruiContext, declaration).also {
            generatedClasses += it
            ruiContext.ruiClasses[it.fqName] = it
        }

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

    }

    private fun rendering(declaration: IrFunction) {

        val ruiClassName = declaration.toRuiClassFqName()

        ruiContext.ruiClasses[ruiClassName]
            ?.transformRendering()
            ?: throw IllegalStateException("missing Rui class: $ruiClassName")

    }


}
