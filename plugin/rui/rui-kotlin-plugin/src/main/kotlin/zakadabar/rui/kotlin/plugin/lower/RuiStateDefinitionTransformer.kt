/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassBuilder
import zakadabar.rui.kotlin.plugin.util.Stack
import zakadabar.rui.kotlin.plugin.util.peek
import zakadabar.rui.kotlin.plugin.util.pop
import zakadabar.rui.kotlin.plugin.util.push

/**
 * State of a component contains:
 *
 * - parameters from the original function, each parameter generates a property in the class
 * - top level variables declared in the function, each variable generates a property in the class
 *
 * Properties generated from original function parameters are passed as
 * constructor parameters.
 *
 * Properties generated from top level variables are initialized by the
 * constructor. The state definition part of the original function is
 * transformed into a constructor.
 *
 * Entry point is [buildStateDefinition].
 */
class RuiStateDefinitionTransformer(
    private val ruiContext: RuiPluginContext,
    private val builder: RuiClassBuilder
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    val irContext = ruiContext.irPluginContext

    val unitType = irContext.irBuiltIns.unitType
    val intType = irContext.irBuiltIns.intType

    val irClass = builder.irClass
    val thisReceiver = irClass.thisReceiver?.symbol ?: throw IllegalStateException("generated class receiver is null")
    val original = builder.original

    /**
     * This stack is used to keep track of variable declarations. Only top level
     * variables are converted into state variables. Values (top lever or not)
     * and not top level variables are kept as they are.
     *
     * Without this we can't make difference between `bb` variables when we
     * reach the `println`.
     *
     * ```
     * @Rui
     * fun Text() {
     *     var bb = 0
     *     if (bb == 0) {
     *        var bb = "hello"
     *        println(bb)
     *     }
     * }
     * ```
     *
     * The bottom of the stack is the top level block, the component itself.
     * As only variables declared at the top level block are moved into state
     * variables, we can check the stack if the given variable is a local
     * one or not.
     */
    val blockStack: Stack<BlockStackEntry> = mutableListOf(BlockStackEntry(true))

    fun String.isStateVariable(): Boolean {
        for (i in blockStack.indices.reversed()) {
            blockStack[i].also {
                if (this in it.variables) return it.top
            }
        }
        // TODO check all the cases variables may be accessed
        return false // this is some global variable
    }

    class BlockStackEntry(
        val top: Boolean = false,
        val variables: MutableList<String> = mutableListOf()
    )

    /**
     * Processes the state definition part of the original function code.
     */
    fun buildStateDefinition() {

        // parameters of the function into constructor parameters and properties

        original.valueParameters.forEach { original ->
            builder.constructor.addValueParameter {
                name = original.name
                type = original.type
                varargElementType = original.varargElementType
            }.also { new ->
                builder.addStateVariable(new)
            }
        }

        // statements of the function into properties and statements in constructor

        original.body?.statements?.let { statements ->
            statements
                .subList(0, builder.boundary)
                .forEach { irStatement ->
                    when (irStatement) {
                        is IrVariable -> addStateVariable(irStatement)
                        else -> builder.initializer.body.statements += irStatement.transformStatement(this)
                    }
                }
        }
    }

    override fun visitVariable(declaration: IrVariable): IrStatement {
        blockStack.peek().variables += declaration.name.identifier
        return super.visitVariable(declaration)
    }

    /**
     * Adds a class property and initializer for this variable.
     *
     * Transforms the initializer code as it may contain references to other
     * variables processed before.
     */
    private fun addStateVariable(irVariable: IrVariable) {
        blockStack[0].variables += irVariable.name.identifier

        builder.addStateVariable(irVariable).also { property ->

            irVariable.initializer?.let {

                val field = checkNotNull(property.backingField)

                builder.initializer.body.statements += IrSetFieldImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    symbol = field.symbol,
                    type = field.type,
                    receiver = irGetReceiver(),
                    value = it.transform(this, null),
                    origin = IrStatementOrigin.INITIALIZE_FIELD
                )
            }
        }

    }

    override fun visitBlock(expression: IrBlock): IrExpression {
        blockStack.push(BlockStackEntry())
        val result = super.visitBlock(expression)
        blockStack.pop()
        return result
    }


    /**
     * Replaces local variable access with class property access.
     *
     * Replaces only top level function variables. Others (one defined in a block)
     * are not reactive, and should not be replaced.
     */
    override fun visitGetValue(expression: IrGetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        if (! name.isStateVariable()) return super.visitGetValue(expression)

        val stateVariable = builder.getStateVariable(name) ?: throw IllegalStateException("missing state variable $name in ${original.name}")

        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            expression.type,
            stateVariable.irProperty.getter !!.symbol,
            0, 0,
        ).apply {
            dispatchReceiver = irGetReceiver()
            return this
        }
    }

    /**
     * Replaces local variable change with class property change.
     *
     * Replaces only top level function variables. Others (one defined in a block)
     * are not reactive, and should not be replaced.
     */
    override fun visitSetValue(expression: IrSetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        if (! name.isStateVariable()) return super.visitSetValue(expression)

        val stateVariable = builder.getStateVariable(name) ?: throw IllegalStateException("missing state variable $name in ${original.name}")

        val property = stateVariable.irProperty
        val field = property.backingField !!

        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            field.type,
            property.setter !!.symbol,
            0, 1
        ).apply {

            dispatchReceiver = irGetReceiver()
            putValueArgument(0, expression.value)

            return this
        }
    }

    fun irGetReceiver() = IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, thisReceiver)

}
