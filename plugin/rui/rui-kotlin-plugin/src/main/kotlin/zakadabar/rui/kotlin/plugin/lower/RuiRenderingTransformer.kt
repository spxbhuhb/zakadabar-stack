/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassCompilation

/**
 * Builds the rendering part of the component. This consists of defining the
 * slots and building the content of the state management functions:
 *
 * - `create`
 * - `patch`
 * - `dispose`
 */
class RuiRenderingTransformer(
    private val ruiContext: RuiPluginContext,
    private val compilation: RuiClassCompilation
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    fun buildRendering() {
        compilation.originalStatements.let { statements ->
            statements
                .subList(compilation.boundary, compilation.originalStatements.size)
                .forEach { irStatement ->
                    when (irStatement) {
                        is IrCall -> addCallStatement(irStatement)
                        is IrBranch -> addBranchStatement(irStatement)
                        is IrLoop -> addLoopStatement(irStatement)
                    }
                }
        }
    }

    private fun addCallStatement(irCall: IrCall) {
        val irFunction = irCall.symbol.owner
        // TODO this exception should be a normal compilation error
        if (! irFunction.isAnnotatedWithRui()) throw IllegalStateException("non-Rui call in rendering")

        compilation.addComponentSlot(irFunction.name.toRuiClassName().asString())
    }

    private fun addBranchStatement(irStatement: IrBranch) {

    }

    private fun addLoopStatement(irStatement: IrLoop) {

    }
}
