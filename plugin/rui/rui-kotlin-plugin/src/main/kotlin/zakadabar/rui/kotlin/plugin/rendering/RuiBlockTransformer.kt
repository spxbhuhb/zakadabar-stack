/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.rendering

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClass
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_INVALID_RENDERING_STATEMENT
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

class RuiBlockTransformer(
    private val ruiContext: RuiPluginContext,
    private val ruiClass: RuiClass
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitBlock(expression: IrBlock): IrExpression {
        for (i in expression.statements.indices) {
            val statement = expression.statements[i]
            when (statement) {
                is IrCall -> ruiClass.addBlock(statement).transform()
                is IrWhen -> ruiClass.addBlock(statement).transform()
                is IrWhileLoop -> ruiClass.addBlock(statement).transform()
                else -> RUI_IR_INVALID_RENDERING_STATEMENT.report(ruiClass, expression)
            }
        }
        return expression
    }

}
