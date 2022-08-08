/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.fromir

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RIU_IR_RENDERING_NON_RUI_CALL
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_INVALID_RENDERING_STATEMENT
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_EXPRESSION_ARGUMENT
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_RENDERING_INVALID_DECLARATION
import zakadabar.rui.kotlin.plugin.model.*
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

class RuiFromIrTransform(
    val ruiContext : RuiPluginContext,
    val irFunction: IrFunction,
    val skipParameters : Int
) : RuiAnnotationBasedExtension {

    lateinit var ruiClass : RuiClass

    var blockIndex = 0
        get() = field ++

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiClass.ruiContext.annotations

    fun IrElement.dependencies(): List<RuiStateVariable> {
        val visitor = RuiDependencyVisitor(ruiClass)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun transform() : RuiClass {
        ruiClass = RuiClass(ruiContext, irFunction)

        RuiStateTransformer(ruiContext, ruiClass, skipParameters).transform()

        transformRoot()

        return ruiClass
    }

    fun transformRoot() {
        val statements = ruiClass.originalStatements

        val irBlock = IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, ruiContext.irContext.irBuiltIns.unitType)
        irBlock.statements.addAll(statements.subList(ruiClass.boundary, statements.size))

        // if this is a single statement, we don't need the surrounding block
        // TODO think about single statement rendering vs. inner transforms / css
        ruiClass.rootBlock = transformBlock(irBlock).let {
            if (it.statements.size == 1) {
                it.statements[0]
            } else {
                it
            }
        }
    }

    fun transformBlock(expression: IrBlock): RuiBlock {

        val ruiBlock = RuiBlock(ruiClass, blockIndex, expression)

        expression.statements.forEach { statement ->
            when (statement) {

                is IrBlock -> {
                    when (statement.origin) {
                        IrStatementOrigin.FOR_LOOP -> transformForLoop(statement)
                        else -> RUI_IR_INVALID_RENDERING_STATEMENT.report(ruiClass, statement)
                    }
                }

                is IrCall -> transformCall(statement)
                is IrWhen -> transformWhen(statement)
                //is IrLoop -> transformLoop(statement)
                else -> RUI_IR_INVALID_RENDERING_STATEMENT.report(ruiClass, statement)

            }?.let {
                ruiBlock.statements += it
            }
        }

        return ruiBlock
    }

    fun transformForLoop(statement: IrBlock): RuiForLoop? {

        // BLOCK type=kotlin.Unit origin=FOR_LOOP
        //          VAR FOR_LOOP_ITERATOR name:tmp0_iterator type:kotlin.collections.IntIterator [val]
        //            CALL 'public open fun iterator (): kotlin.collections.IntIterator [fake_override,operator] declared in kotlin.ranges.IntRange' type=kotlin.collections.IntIterator origin=FOR_LOOP_ITERATOR
        //              $this: CALL 'public final fun rangeTo (other: kotlin.Int): kotlin.ranges.IntRange [operator] declared in kotlin.Int' type=kotlin.ranges.IntRange origin=RANGE
        //                $this: CONST Int type=kotlin.Int value=0
        //                other: CONST Int type=kotlin.Int value=10
        //          WHILE label=null origin=FOR_LOOP_INNER_WHILE
        //            condition: CALL 'public abstract fun hasNext (): kotlin.Boolean [fake_override,operator] declared in kotlin.collections.IntIterator' type=kotlin.Boolean origin=FOR_LOOP_HAS_NEXT
        //              $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in zakadabar.rui.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //            body: BLOCK type=kotlin.Unit origin=FOR_LOOP_INNER_WHILE
        //              VAR FOR_LOOP_VARIABLE name:i type:kotlin.Int [val]
        //                CALL 'public final fun next (): kotlin.Int [operator] declared in kotlin.collections.IntIterator' type=kotlin.Int origin=FOR_LOOP_NEXT
        //                  $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in zakadabar.rui.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //              BLOCK type=kotlin.Unit origin=null
        //                CALL 'public final fun P1 (p0: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin' type=kotlin.Unit origin=null
        //                  p0: GET_VAR 'val i: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.successes.Basic' type=kotlin.Int origin=null

        // TODO convert checks into non-exception throwing, but contracting functions
        check(statement.statements.size == 2)

        val irIterator = statement.statements[0]
        val loop = statement.statements[1]

        check(irIterator is IrValueDeclaration && irIterator.origin == IrDeclarationOrigin.FOR_LOOP_ITERATOR)
        check(loop is IrWhileLoop && loop.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)

        val condition = transformExpression(loop.condition, RuiExpressionOrigin.FOR_LOOP_CONDITION)

        val body = loop.body

        check(body is IrBlock && body.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)
        check(body.statements.size == 2)

        val irLoopVariable = body.statements[0]
        val block = body.statements[1]

        check(irLoopVariable is IrVariable)
        check(block is IrBlock && block.origin == null)

        val iterator = transformDeclaration(irIterator, RuiDeclarationOrigin.FOR_LOOP_ITERATOR) ?: return null
        val loopVariable = transformDeclaration(irLoopVariable, RuiDeclarationOrigin.FOR_LOOP_VARIABLE) ?: return null

        return RuiForLoop(
            ruiClass, blockIndex, statement,
            iterator,
            condition,
            loopVariable,
            transformBlock(block)
        )
    }

    fun transformCall(statement: IrCall): RuiCall? {

        if (! statement.symbol.owner.isAnnotatedWithRui()) {
            return RIU_IR_RENDERING_NON_RUI_CALL.report(ruiClass, statement)
        }

        val ruiCall = RuiCall(ruiClass, blockIndex, statement)

        for (index in 0 until statement.valueArgumentsCount) {

            val expression = statement.getValueArgument(index)
                ?: return RUI_IR_MISSING_EXPRESSION_ARGUMENT.report(ruiClass, statement)

            ruiCall.valueArguments += transformValueArgument(index, expression)
        }

        return ruiCall
    }

    fun transformWhen(statement: IrWhen): RuiWhen {
        val ruiWhen = RuiWhen(ruiClass, blockIndex, statement)

        statement.branches.forEach { irBranch ->
            ruiWhen.branches += transformBranch(irBranch)
        }

        return ruiWhen
    }

    fun transformBranch(branch: IrBranch): RuiBranch {
        return RuiBranch(
            ruiClass, blockIndex,
            branch,
            transformExpression(branch.condition, RuiExpressionOrigin.BRANCH_CONDITION),
            transformExpression(branch.result, RuiExpressionOrigin.BRANCH_RESULT),
        )
    }

    fun transformValueArgument(index : Int, expression: IrExpression): RuiExpression {
        return RuiValueArgument(ruiClass, index, expression, expression.dependencies())
    }

    fun transformExpression(expression: IrExpression, origin : RuiExpressionOrigin): RuiExpression {
        return RuiExpression(ruiClass, expression, origin, expression.dependencies())
    }

    fun transformDeclaration(declaration: IrDeclaration, origin : RuiDeclarationOrigin): RuiDeclaration? =
        when (declaration) {
            is IrValueDeclaration -> RuiDeclaration(ruiClass, declaration, origin, declaration.dependencies())
            else -> RUI_IR_RENDERING_INVALID_DECLARATION.report(ruiClass, declaration)
        }

}