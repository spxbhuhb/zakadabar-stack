/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClass
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_RENDERING_VARIABLE
import zakadabar.rui.kotlin.plugin.util.Stack
import zakadabar.rui.kotlin.plugin.util.peek
import zakadabar.rui.kotlin.plugin.util.pop
import zakadabar.rui.kotlin.plugin.util.push

/**
 * Transforms:
 *
 * - top-level local variables into state variables (properties)
 * - variable access into property getter call
 * - variable change into property setter call + invalidate call
 *
 */
class RuiStateVariableTransformer(
    private val ruiContext: RuiPluginContext,
    private val ruiClass: RuiClass
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    var currentStatementIndex = 0

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    fun buildStateDefinition() {
        // add function parameters to known variables, without this access of these
        // variables wouldn't be transformed into property access
        ruiClass.stateVariables.forEach {
            blockStack[0].variables += it.value.originalName
        }

        ruiClass.originalStatements.forEachIndexed { statementIndex, irStatement ->
            currentStatementIndex = statementIndex
            when (irStatement) {
                is IrVariable -> addStateVariable(irStatement)
                else -> ruiClass.initializer.body.statements += irStatement.transformStatement(this)
            }
        }
    }

    private fun addStateVariable(irVariable: IrVariable) {
        blockStack[0].variables += irVariable.name.identifier

        RUI_IR_RENDERING_VARIABLE.check(ruiClass, irVariable) { currentStatementIndex < ruiClass.boundary }

        // the initializer may use other variables, so we have to transform it as well
        irVariable.initializer?.let { initializer ->
            irVariable.initializer = initializer.transform(this, null)
        }

        ruiClass.addStateVariable(irVariable)
    }

    override fun visitVariable(declaration: IrVariable): IrStatement {
        blockStack.peek().variables += declaration.name.identifier

        RUI_IR_RENDERING_VARIABLE.check(ruiClass, declaration) { currentStatementIndex < ruiClass.boundary }

        return super.visitVariable(declaration)
    }

    override fun visitBlock(expression: IrBlock): IrExpression {
        blockStack.push(BlockStackEntry())
        val result = super.visitBlock(expression)
        blockStack.pop()
        return result
    }

    override fun visitValueParameterNew(declaration: IrValueParameter): IrStatement {
        return super.visitValueParameterNew(declaration)
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

        return ruiClass.getStateVariable(name)
            ?.irGetValue()
            ?: throw IllegalStateException("missing state variable $name in ${ruiClass.original.name}")
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

        return ruiClass.getStateVariable(name)
            ?.irSetValue(expression.value)
            ?: throw IllegalStateException("missing state variable $name in ${ruiClass.original.name}")
    }

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

}
