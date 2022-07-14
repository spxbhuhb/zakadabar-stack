/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.extensions.AnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildVariable
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.isInt
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.copyTypeAndValueArgumentsFrom
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext

class RuiTransformerOld(
    private val context: IrPluginContext,
    private val configuration: RuiPluginContext
) : IrElementTransformerVoidWithContext(), AnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        configuration.annotations

    val intClass = context.symbols.int
    val intType = intClass.defaultType
    val unitType = context.irBuiltIns.unitType

    // Number of synthetic parameters for each modified function

    val reactiveSyntheticParameterCount = 2
    val optimizeSyntheticParameterCount = 3

    // Find variants of the optimize function

    val funOptimizeVariants = context.referenceFunctions(FqName.fromSegments(listOf("zakadabar", "reactive", "core", "optimize")))
    fun funOptimizeWithParams(count: Int) = funOptimizeVariants.single { it.owner.valueParameters.size == optimizeSyntheticParameterCount + count }

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

        // When the original function already has the reactive parameters we do not have
        // to add them again.

        if (!hasReactiveParameters(declaration)) {
            declaration.addValueParameter("\$callSiteOffset", context.irBuiltIns.intType)
            declaration.addValueParameter("\$parentState", reactiveStateType)
        }

        // Create a new body for the function with reactive code before and after the
        // original body. "buildState" is the state that belongs to the function. It
        // is returned by "optimize" and is passed to all reactive function calls.

        var buildState : IrVariable? = null

        declaration.body = DeclarationIrBuilder(context, declaration.symbol).irBlockBody {

            buildState = irReactiveEnter(declaration)
            for (statement in declaration.body !!.statements) + statement
            + irReactiveLeave(declaration)

        }

        // Transform all reactive calls: add the call site and the current build state
        // to the call.

        declaration.transformChildren(object : IrElementTransformerVoid() {

            override fun visitCall(expression: IrCall): IrExpression {
                if (! isReactive(expression)) return super.visitCall(expression)

                // When the original function declaration has the reactive parameters the call
                // already contains those, so there is no need to add them again.

                if (hasReactiveParameters(expression.symbol.owner)) return super.visitCall(expression)

                return IrCallImpl(
                    expression.startOffset,
                    expression.endOffset,
                    expression.type,
                    expression.symbol,
                    expression.typeArgumentsCount,
                    expression.valueArgumentsCount + reactiveSyntheticParameterCount,
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
                            UNDEFINED_OFFSET,
                            UNDEFINED_OFFSET,
                            buildState!!.symbol
                        )
                    )
                }

            }
        }, null)

        return super.visitFunctionNew(declaration)
    }

    /**
     * Builds the code executed before the original body of the reactive function is
     * executed:
     *
     * - calls the optimize function
     * - if optimize
     *   - returns with null: the old render is still valid, so the original body won't be executed
     *   - returns with a state: the state is saved and the original body will be executed
     */
    private fun IrBlockBodyBuilder.irReactiveEnter(function: IrFunction) : IrVariable {
        val parameters = function.valueParameters
        val originalParameterCount = parameters.size - reactiveSyntheticParameterCount

        require(originalParameterCount <= funOptimize.size) { "reactive compiler plugin parameter number limit is ${funOptimize.size}, cannot compile ${function.name} with $originalParameterCount parameters" }

        val call = irCall(funOptimize[originalParameterCount]).apply {
            putValueArgument(0, function.name.asString().toIrConst(context.irBuiltIns.stringType))
            putValueArgument(1, irGet(parameters[parameters.size - 2]))
            putValueArgument(2, irGet(parameters.last()))

            var idx = 0
            while (idx < parameters.size - reactiveSyntheticParameterCount) {
                putValueArgument(idx + optimizeSyntheticParameterCount, irGet(parameters[idx + reactiveSyntheticParameterCount]))
                idx++
            }
        }

        val buildState = buildVariable(
            scope.getLocalDeclarationParent(), function.startOffset, function.endOffset,
            IrDeclarationOrigin.IR_TEMPORARY_VARIABLE, Name.identifier("\$buildState"),
            reactiveStateType.makeNullable()
        ).apply {
            initializer = call
        }

        + buildState
        + irIfNull(unitType, irGet(buildState), irReturnUnit(), irUnit())

        return buildState
    }

    /**
     * Builds code to execute after the original body of the reactive function is executed.
     *
     * - calls the "lastChildCurrentToFuture" function with the parent state
     */
    private fun IrBuilderWithScope.irReactiveLeave(function: IrFunction): IrCall {
        val parameters = function.valueParameters

        return irCall(funLastChildCurrentToFuture).apply {
            putValueArgument(0, irGet(parameters.last()))
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

    private fun hasReactiveParameters(declaration: IrFunction): Boolean {
        val parameters = declaration.valueParameters

        if (parameters.size < 2) return false
        if (! parameters[parameters.size - 2].type.isInt()) return false
        if (parameters.last().type != reactiveStateType) return false

        return true
    }

}
