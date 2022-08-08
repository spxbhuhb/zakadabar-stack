/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.fromir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_FUNCTION_BODY
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.RUI_ENTRY_FUNCTION
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension
import kotlin.collections.set

class RuiFunctionVisitor(
    private val ruiContext: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    val ruiClasses = mutableListOf<RuiClass>()

    val irBuiltIns = ruiContext.irContext.irBuiltIns

    val irFactory = ruiContext.irContext.irFactory

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
        if (expression.symbol.owner.kotlinFqName != RUI_ENTRY_FUNCTION) {
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

        if (adapter.type != ruiContext.ruiAdapterType && !adapter.type.superTypes().contains(ruiContext.ruiAdapterType)) {
            return report("${expression.symbol} first parameter is not a RuiAdapter")
        }

        val ruiClass: RuiClass

        // skip the ruiAdapter function parameter
        RuiFromIrTransform(ruiContext, function, skipParameters = 1).transform().also {
            ruiClasses += it
            ruiContext.ruiClasses[it.irClass.kotlinFqName] = it
            ruiClass = it
        }

        function.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).also {

            it.statements += IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                ruiClass.irClass.defaultType,
                ruiClass.builder.constructor.symbol,
                0, 0, 2
            ).also { call ->
                call.putValueArgument(0, buildGetAdapter(function))
                call.putValueArgument(1, buildExternalPatch(function.symbol))
            }

        }

        if (RuiPluginContext.DUMP_KOTLIN_LIKE in ruiContext.dumpPoints) {
            println(block.function.dumpKotlinLike())
        }

        return expression
    }

    private fun buildGetAdapter(function: IrSimpleFunction): IrExpression =
        IrGetValueImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            function.valueParameters.first().symbol
        )

    fun buildExternalPatch(parent: IrSimpleFunctionSymbol): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = irBuiltIns.unitType
        }.also { function ->

            function.parent = parent.owner
            function.visibility = DescriptorVisibilities.LOCAL

            function.addValueParameter {
                name = Name.identifier("it")
                type = ruiContext.ruiFragmentType
            }

            function.body = DeclarationIrBuilder(ruiContext.irContext, function.symbol).irBlockBody { }
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            ruiContext.ruiExternalPatchType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }

}
