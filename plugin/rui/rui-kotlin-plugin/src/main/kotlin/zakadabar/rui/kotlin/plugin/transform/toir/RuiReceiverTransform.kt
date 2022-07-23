/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.toir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.deepCopyWithVariables
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.transform.builders.RuiBuilder
import zakadabar.rui.kotlin.plugin.transform.builders.RuiClassBuilder
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

/**
 * Transforms `this` references into an inner scope.
 */
class RuiReceiverTransform(
    override val ruiClassBuilder: RuiClassBuilder,
    private val fromReceiver: IrClass,
    private val toReceiver : IrSimpleFunction
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension, RuiBuilder {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    fun copyAndTransform(irExpression : IrExpression) : IrExpression {
        return irExpression.deepCopyWithVariables().transform(this, null)
    }

    override fun visitGetValue(expression: IrGetValue): IrExpression {

        if (expression.symbol != fromReceiver.thisReceiver!!.symbol) return expression

        return IrGetValueImpl(
            expression.startOffset,
            expression.endOffset,
            expression.type,
            toReceiver.dispatchReceiverParameter!!.symbol,
            expression.origin
        )
    }

}
