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
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.copyTypeAndValueArgumentsFrom
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtModifierListOwner

class ReactiveIrTransformer(
    private val context: IrPluginContext,
    private val annotations: List<String>
) : IrElementTransformerVoidWithContext(), AnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        annotations

    val intClass = context.symbols.int
    val intType = intClass.defaultType

    // Find variants of the optimize function

    val funOptimizeVariants = context.referenceFunctions(FqName.fromSegments(listOf("zakadabar", "reactive", "core", "optimize")))
    fun funOptimizeWithParams(count: Int) = funOptimizeVariants.single { it.owner.valueParameters.size == 2 + count }

    val funOptimize = arrayOf(
        funOptimizeWithParams(0),
        funOptimizeWithParams(1)
    )

    // Find the lastChildCurrentToFuture function

    val funLastChildCurrentToFuture = context
        .referenceFunctions(FqName.fromSegments(listOf("zakadabar", "reactive", "core", "lastChildCurrentToFuture")))
        .single { it.owner.valueParameters.size == 1 }

    // Find the ReactiveState type

    val reactiveStateType = context
        .referenceClass(FqName.fromSegments(listOf("zakadabar", "reactive", "core", "ReactiveState"))) !!
        .typeWith()

    var currentReactiveScope : IrFunction? = null

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
        if (! isReactive(declaration)) return super.visitFunctionNew(declaration)

        declaration.addValueParameter("callSiteOffset", context.irBuiltIns.intType)
        declaration.addValueParameter("parentState", context.irBuiltIns.intType)
        //declaration.addValueParameter("parentState", reactiveStateType.typeWith())

        declaration.body = irReactive(declaration)

        val oldScope = currentReactiveScope
        currentReactiveScope = declaration
        val statement = super.visitFunctionNew(declaration)
        currentReactiveScope = oldScope
        return statement
    }

    /**
     * Modifies a function marked with "Reactive" such a way that it first executes
     * some statements (build by [irReactiveEnter]) and then executes the original
     * statements.
     */
    private fun irReactive(
        function: IrFunction,
    ): IrBlockBody {
        return DeclarationIrBuilder(context, function.symbol)
            .irBlockBody {
                + irReactiveEnter(function)
                for (statement in function.body !!.statements) + statement
            }
    }

    /**
     * Builds pre-processing code for functions marked with the "Reactive" annotation.
     * For now, it calls the "whatever" function with the call site as the first parameter.
     */
    private fun IrBuilderWithScope.irReactiveEnter(function: IrFunction): IrCall {
        val parameters = function.valueParameters
        val originalParameterCount = parameters.size - 2 // callSiteOffset and parentState

        require(originalParameterCount <= funOptimize.size) { "reactive compiler plugin parameter number limit is ${funOptimize.size}, cannot compile ${function.name} with $originalParameterCount parameters" }

        val call = irCall(funOptimize[originalParameterCount])

        call.putValueArgument(0, irGet(parameters[parameters.size - 2]))
        call.putValueArgument(1, irGet(parameters.last()))

        for (i in 2 until parameters.size) {
            call.putValueArgument(i, irGet(parameters[i-2]))
        }

        return call
    }


    /**
     * Adds the call site offset to a reactive function call.
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (! isReactive(expression)) return super.visitCall(expression)

        return IrCallImpl(
            expression.startOffset,
            expression.endOffset,
            expression.type,
            expression.symbol,
            expression.typeArgumentsCount,
            expression.valueArgumentsCount + 2,
            expression.origin,
            expression.superQualifierSymbol
        ).also {
            it.copyTypeAndValueArgumentsFrom(expression)
            it.putValueArgument(
                index = expression.valueArgumentsCount,
                expression.startOffset.toIrConst(intType)
            )
            it.putValueArgument(
                index = expression.valueArgumentsCount + 1,
                IrGetValueImpl(
                    expression.startOffset,
                    expression.endOffset,
                    reactiveStateType,
                    currentReactiveScope!!.valueParameters.last().symbol,
                    expression.origin
                )
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
