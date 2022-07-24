/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.fromir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_RENDERING_VARIABLE
import zakadabar.rui.kotlin.plugin.model.*
import zakadabar.rui.kotlin.plugin.util.*

/**
 * Transforms state variable accesses from the original code into property
 * access in the generated IrClass.
 *
 * The [blockStack] is used to keep track of variable declarations. Only top
 * level variables are converted into state variables. Values (top lever or not)
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
class RuiStateTransformer(
    private val ruiContext: RuiPluginContext,
    private val ruiClass: RuiClass
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    var currentStatementIndex = 0

    var stateVariableIndex = 0
        get() = field ++

    val blockStack: Stack<BlockStackEntry> = mutableListOf(BlockStackEntry(true))

    class BlockStackEntry(
        val top: Boolean = false,
        val variables: MutableList<String> = mutableListOf()
    )

    fun String.isStateVariable(): Boolean {
        for (i in blockStack.indices.reversed()) {
            blockStack[i].also {
                if (this in it.variables) return it.top
            }
        }
        // TODO check all the cases variables may be accessed
        return false // this is some global variable
    }

    fun transform() {

        ruiClass.irFunction.valueParameters.forEach { valueParameter ->
            RuiExternalStateVariable(ruiClass, stateVariableIndex, valueParameter).also {
                register(it)
                addDirtyMask(it)
            }
        }

        ruiClass.originalStatements.forEachIndexed { statementIndex, irStatement ->

            if (irStatement !is IrVariable) return@forEachIndexed

            if (statementIndex >= ruiClass.boundary) {
                RUI_IR_RENDERING_VARIABLE.report(ruiClass, irStatement)
                return@forEachIndexed
            }

            RuiInternalStateVariable(ruiClass, stateVariableIndex, irStatement).also {
                register(it)
                addDirtyMask(it)
            }

        }

        ruiClass.originalStatements.forEachIndexed { index, statement ->
            currentStatementIndex = index
            if (index < ruiClass.boundary) {
                ruiClass.initializerStatements += statement.transform(this, null) as IrStatement
            } else {
                ruiClass.renderingStatements += statement.transform(this, null) as IrStatement
            }
        }
    }

    fun register(it: RuiStateVariable) {
        val root = blockStack[0]
        ruiClass.stateVariables[it.originalName] = it
        root.variables += it.originalName
        ruiClass.symbolMap[it.builder.getter.symbol] = it
    }

    fun addDirtyMask(it: RuiStateVariable) {
        val maskNumber = it.index / 32
        if (ruiClass.dirtyMasks.size > maskNumber) return
        ruiClass.dirtyMasks += RuiDirtyMask(ruiClass, maskNumber)
    }

    override fun visitVariable(declaration: IrVariable): IrStatement {
        blockStack.peek().variables += declaration.name.identifier

        RUI_IR_RENDERING_VARIABLE.check(ruiClass, declaration) {
            currentStatementIndex < ruiClass.boundary ||
                    declaration.origin == IrDeclarationOrigin.FOR_LOOP_ITERATOR ||
                    declaration.origin == IrDeclarationOrigin.FOR_LOOP_VARIABLE
        }

        return super.visitVariable(declaration)
    }

    override fun visitBlock(expression: IrBlock): IrExpression {
        blockStack.push(BlockStackEntry())
        val result = super.visitBlock(expression)
        blockStack.pop()
        return result
    }

    override fun visitGetValue(expression: IrGetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        if (! name.isStateVariable()) return super.visitGetValue(expression)

        return ruiClass.stateVariables[name]
            ?.builder?.irGetValue()
            ?: throw IllegalStateException("missing state variable $name in ${ruiClass.irFunction.name}")
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

        return ruiClass.stateVariables[name]
            ?.builder?.irSetValue(expression.value)
            ?: throw IllegalStateException("missing state variable $name in ${ruiClass.irFunction.name}")
    }

}
