/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.extensions.AnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.copyTypeAndValueArgumentsFrom
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtModifierListOwner

class ReactiveIrTransformer(
    private val context: IrPluginContext,
    private val annotations: List<String>
) : IrElementTransformerVoidWithContext(), AnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        annotations

    val intClass = context.symbols.int
    val intType = intClass.defaultType

    val funWhatever = context
        .referenceFunctions(FqName.ROOT.child(Name.identifier("whatever")))
        .single {
            val parameters = it.owner.valueParameters
            parameters.size == 1 && parameters[0].type == intType
        }

    /**
     * Adds a new parameter "callSiteOffset" to all reactive functions. This makes
     * it possible to uniquely identify all calls of a reactive function. For example,
     * these two calls are identical without the call site information.
     *
     * ```kotlin
     * Button(label = "button label")
     * Button(label = "button label")
     * ```
     *
     * This function converts the reactive function like this:
     *
     * ```kotlin
     * fun Button(label : String) {  }
     *
     * fun Button(label: String, callSiteOffset : Int) {  }
     * ```
     */
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        if (!isReactive(declaration)) return super.visitFunctionNew(declaration)

        declaration.addValueParameter("callSiteOffset", context.irBuiltIns.intType)

        declaration.body = irReactive(declaration)

        return super.visitFunctionNew(declaration)
    }

    /**
     * Modifies a function marked with "Reactive" such a way that it first executes
     * some statements (build by [irReactiveEnter]) and then executes the original
     * statements.
     */
    private fun irReactive(
        function: IrFunction,
    ): IrBlockBody {
        return DeclarationIrBuilder(context, function.symbol).irBlockBody {
            +irReactiveEnter(function)
            for (statement in function.body!!.statements) +statement
        }
    }

    /**
     * Builds pre-processing code for functions marked with the "Reactive" annotation.
     * For now, it calls the "whatever" function with the call site as the first parameter.
     */
    private fun IrBuilderWithScope.irReactiveEnter(
        function: IrFunction
    ): IrCall {
        return irCall(funWhatever)
            .also { call ->
                call.putValueArgument(0, irGet(function.valueParameters.last()))
            }
    }

    /**
     * Adds the call site offset to a reactive function call.
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (!isReactive(expression)) return super.visitCall(expression)

        return IrCallImpl(
            expression.startOffset,
            expression.endOffset,
            expression.type,
            expression.symbol,
            expression.typeArgumentsCount,
            expression.valueArgumentsCount + 1,
            expression.origin,
            expression.superQualifierSymbol
        ).also {
            it.copyTypeAndValueArgumentsFrom(expression)
            it.putValueArgument(
                index = expression.valueArgumentsCount,
                expression.startOffset.toIrConst(intType)
            )
        }
    }

    private fun isReactive(declaration: IrClass): Boolean =
        declaration.kind == ClassKind.CLASS &&
            declaration.isAnnotatedWithReactive()

    private fun isReactive(declaration: IrFunction): Boolean =
        declaration.isAnnotatedWithReactive()

    private fun isReactive(expression: IrCall): Boolean =
        expression.symbol.owner.isAnnotatedWithReactive()

    private fun isReactive(declaration: IrSimpleFunctionSymbol): Boolean =
        declaration.owner.isAnnotatedWithReactive()

    private fun IrClass.isAnnotatedWithReactive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    private fun IrFunction.isAnnotatedWithReactive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)
}
