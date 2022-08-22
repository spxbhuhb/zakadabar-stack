/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.fromir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.superTypes
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RUI_FQN_ENTRY_FUNCTION
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_FUNCTION_BODY
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.model.RuiEntryPoint
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension
import kotlin.collections.set

class RuiFunctionVisitor(
    private val ruiContext: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    val ruiClasses = mutableListOf<RuiClass>()
    val ruiEntryPoints = mutableListOf<RuiEntryPoint>()

    val irBuiltIns = ruiContext.irContext.irBuiltIns

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    /**
     * Transforms a function annotated with `@Rui` into a Rui component class.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (! declaration.isAnnotatedWithRui()) {
            return super.visitFunctionNew(declaration) as IrFunction
        }

        if (declaration.body == null) {
            RUI_IR_MISSING_FUNCTION_BODY.report(ruiContext, declaration)
            return super.visitFunctionNew(declaration) as IrFunction
        }

        RuiFromIrTransform(ruiContext, declaration, 0).transform().also {
            ruiClasses += it
            ruiContext.ruiClasses[it.irClass.kotlinFqName] = it
        }

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
    }

    /**
     * Transforms Rui entry points (calls to the function `rui`) into a
     * Rui root class and modifies the last parameter of the function (which
     * has to be a lambda) so it gets an adapter and creates a new instance
     * of the root class.
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol.owner.kotlinFqName != RUI_FQN_ENTRY_FUNCTION) {
            return super.visitCall(expression)
        }

        fun report(message: String): IrExpression {
            ErrorsRui.RUI_IR_INTERNAL_PLUGIN_ERROR.report(ruiContext, currentFile.fileEntry, expression.startOffset, message)
            return super.visitCall(expression)
        }

        if (expression.valueArgumentsCount == 0) {
            return report("${expression.symbol} value arguments count == 0")
        }

        val block = expression.getValueArgument(expression.valueArgumentsCount - 1)

        if (block !is IrFunctionExpression) {
            return report("${expression.symbol} last parameter is not a function expression")
        }

        val function = block.function

        if (function.valueParameters.isEmpty()) {
            return report("${expression.symbol} does not have RuiAdapter as first parameter")
        }

        val adapter = function.valueParameters.first()

        val classifier = ruiContext.ruiAdapterType.classifierOrNull

        if (adapter.type.classifierOrNull != classifier) {
            if (adapter.type.superTypes().firstOrNull { it.classifierOrNull == classifier } == null) {
                return report("${expression.symbol} first parameter is not a RuiAdapter")
            }
        }

        val ruiClass: RuiClass

        // skip the ruiAdapter function parameter
        RuiFromIrTransform(ruiContext, function, skipParameters = 1).transform().also {
            ruiClasses += it
            ruiContext.ruiClasses[it.irClass.kotlinFqName] = it
            ruiClass = it
        }

        RuiEntryPoint(ruiClass, function).also {
            ruiEntryPoints += it
            ruiContext.ruiEntryPoints += it
        }

        return expression
    }

}
