/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.rendering

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.builder.*
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RIU_IR_RENDERING_NON_RUI_CALL
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_INVALID_RENDERING_STATEMENT
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_EXPRESSION_ARGUMENT
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_RENDERING_INVALID_DECLARATION
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

class RuiFromIrTransform(
    private val ruiClass: RuiClass,
) : RuiAnnotationBasedExtension {

    var index = 0
        get() = field ++

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiClass.ruiContext.annotations

    fun IrElement.dependencies(): List<RuiStateVariable> {
        val visitor = RuiDependencyVisitor(ruiClass)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun transformClass(ruiClass: RuiClass) {
        val statements = ruiClass.originalStatements

        val irBlock = IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, ruiClass.irContext.irBuiltIns.unitType)
        irBlock.statements.addAll(statements.subList(ruiClass.boundary, statements.size))

        ruiClass.rootBlock = transformBlock(irBlock)
    }

    fun transformBlock(expression: IrBlock): RuiBlock {

        val ruiBlock = RuiBlock(ruiClass, index, expression)

        expression.statements.forEach { statement ->
            when (statement) {

                is IrBlock -> {
                    when (statement.origin) {
                        IrStatementOrigin.FOR_LOOP -> transformForBlock(statement)
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

    fun transformForBlock(statement: IrBlock): RuiForLoop? {

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

        val condition = transformExpression(loop.condition)

        val body = loop.body

        check(body is IrBlock && body.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)
        check(body.statements.size == 2)

        val irLoopVariable = body.statements[0]
        val block = body.statements[1]

        check(irLoopVariable is IrVariable)
        check(block is IrBlock && block.origin == null)

        val iterator = transformDeclaration(irIterator) ?: return null
        val loopVariable = transformDeclaration(irLoopVariable) ?: return null

        return RuiForLoop(
            ruiClass, index, statement,
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

        val ruiCall = RuiCall(ruiClass, index, statement)

        for (index in 0 until statement.valueArgumentsCount) {

            val expression = statement.getValueArgument(index)
                ?: return RUI_IR_MISSING_EXPRESSION_ARGUMENT.report(ruiClass, statement)

            ruiCall.valueArguments += RuiExpression(
                ruiClass, index,
                expression,
                expression.dependencies()
            )
        }

        return ruiCall
    }

    fun transformWhen(statement: IrWhen): RuiWhen {
        val ruiWhen = RuiWhen(ruiClass, index, statement)

        statement.branches.forEach { irBranch ->
            ruiWhen.branches += transformBranch(irBranch)
        }

        return ruiWhen
    }

//    fun transformLoop(statement: IrLoop): RuiForLoop? {
//
//        val irBody = statement.body
//
//        val ruiBody = when (irBody) {
//            is IrBlock -> transformBlock(irBody)
//            is IrCall -> transformCall(irBody)
//            is IrWhen -> transformWhen(irBody)
//            is IrLoop -> transformLoop(irBody)
//            null -> RIU_IR_RENDERING_NO_LOOP_BODY.report(ruiClass, statement)
//            else -> RUI_IR_RENDERING_INVALID_LOOP_BODY.report(ruiClass, statement)
//        } ?: return null
//
//        val condition = transformExpression(statement.condition)
//
//        return RuiForLoop(ruiClass, index, statement, ruiBody?, condition)
//    }

    fun transformBranch(branch: IrBranch): RuiBranch {
        return RuiBranch(
            ruiClass, index,
            branch,
            RuiExpression(ruiClass, index, branch.condition, branch.condition.dependencies()),
            RuiExpression(ruiClass, index, branch.result, branch.result.dependencies()),
        )
    }

    fun transformExpression(expression: IrExpression): RuiExpression {
        return RuiExpression(ruiClass, index, expression, expression.dependencies())
    }

    fun transformDeclaration(declaration: IrDeclaration): RuiDeclaration? =
        when (declaration) {
            is IrValueDeclaration -> RuiDeclaration(ruiClass, index, declaration, declaration.dependencies())
            else -> RUI_IR_RENDERING_INVALID_DECLARATION.report(ruiClass, declaration)
        }

}