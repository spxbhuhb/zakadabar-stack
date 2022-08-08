/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.fromir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_RENDERING_VARIABLE
import zakadabar.rui.kotlin.plugin.model.*
import zakadabar.rui.kotlin.plugin.util.*
import kotlin.collections.set

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
 *
 * @property  skipParameters  Skip the first N parameter of the original function when
 *                            converting the function parameters into external state variables.
 *                            Used for entry points when the first parameter is the adapter.
 */
class RuiStateTransformer(
    private val ruiContext: RuiPluginContext,
    private val ruiClass: RuiClass,
    private val skipParameters : Int
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    val irContext = ruiContext.irContext

    val irBuiltIns = irContext.irBuiltIns

    var currentStatementIndex = 0

    var stateVariableIndex = 0
        get() = field ++

    val blockStack: Stack<BlockStackEntry> = mutableListOf(BlockStackEntry(true))

    var lastPop: BlockStackEntry = blockStack[0]

    val rendering
        get() = currentStatementIndex > ruiClass.boundary

    /**
     * @property  top                   True means first block, the original function itself.
     * @property  stateVariableChange   True means that this block does change state variables.
     * @property  functionOrLambda      True means that this block is part of a function or lambda declaration.
     * @property  variables             Names of variables declared in this block.
     */
    class BlockStackEntry(
        val top: Boolean = false,
        var stateVariableChange: Boolean = false,
        var functionOrLambda: Boolean = false,
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

        ruiClass.irFunction.valueParameters.forEachIndexed { index, valueParameter ->

            if (index < skipParameters) return@forEachIndexed

            RuiExternalStateVariable(ruiClass, stateVariableIndex, valueParameter).also {
                register(it)
                addDirtyMask(it)
            }
        }

        ruiClass.originalStatements.forEachIndexed { index, irStatement ->

            currentStatementIndex = index

            if (irStatement is IrVariable) {
                transformStateVariable(irStatement)
            } else {
                transformNotVariable(irStatement)
            }
        }
    }

    fun transformStateVariable(irStatement: IrVariable) {
        if (currentStatementIndex >= ruiClass.boundary) {
            RUI_IR_RENDERING_VARIABLE.report(ruiClass, irStatement)
            return
        }

        RuiInternalStateVariable(ruiClass, stateVariableIndex, irStatement).also {
            register(it)
            addDirtyMask(it)
        }
    }

    fun transformNotVariable(irStatement : IrStatement) {
        if (currentStatementIndex < ruiClass.boundary) {
            ruiClass.initializerStatements += irStatement.transform(this, null) as IrStatement
        } else {
            ruiClass.renderingStatements += irStatement.transform(this, null) as IrStatement
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

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        val transformed = super.visitFunctionNew(declaration) as IrFunction

        if (! lastPop.stateVariableChange) return transformed

        val ps = parentScope

        when {
            ps == null -> return irPatch(transformed)
            ps.irElement is IrProperty -> TODO()
            ps.irElement is IrClass -> TODO()
            else -> return transformed
        }
    }

    fun irPatch(function: IrFunction): IrFunction {
        val body = function.body ?: return function

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            for (statement in body.statements) + statement
            + irCallOp(
                ruiClass.builder.patch.symbol,
                ruiClass.builder.irBuiltIns.unitType,
                ruiClass.builder.irThisReceiver()
            )
        }

        return function
    }

    override fun visitBlock(expression: IrBlock): IrExpression {
        blockStack.push(BlockStackEntry())
        val result = super.visitBlock(expression)
        lastPop = blockStack.pop()
        return result
    }

    override fun visitBlockBody(body: IrBlockBody): IrBody {
        blockStack.push(BlockStackEntry())
        val result = super.visitBlockBody(body)
        lastPop = blockStack.pop()
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

        if (currentScope == null) return super.visitSetValue(expression)

        // Sets all but the top entries in the block stack as state variable
        // changing block.

        var idx = blockStack.lastIndex
        while (idx > 0) {
            blockStack[idx].stateVariableChange = true
            idx --
        }

        return DeclarationIrBuilder(irContext, currentScope !!.scope.scopeOwnerSymbol).irComposite {
            val stateVariable = ruiClass.stateVariables[name] ?: throw IllegalStateException("missing state variable $name in ${ruiClass.irFunction.name}")

            if (ruiContext.withTrace) {
                + stateVariable.builder.irSetValue(traceStateChange(expression, stateVariable))
            } else {
                + stateVariable.builder.irSetValue(expression.value)
            }

            + irCallOp(
                ruiClass.dirtyMasks[stateVariable.index / 32].builder.invalidate,
                irBuiltIns.unitType,
                ruiClass.builder.irThisReceiver(),
                (stateVariableIndex % 32).toIrConst(irBuiltIns.intType)
            )
        }
    }

    fun IrBlockBuilder.traceStateChange(expression : IrSetValue, stateVariable : RuiStateVariable) : IrExpression {
        val newValue = irTemporary(expression.value)

        val concat = irConcat()
        concat.addArgument(irString(traceLabel(ruiClass.name, "state change")))
        concat.addArgument(irString(" ${stateVariable.name}: "))
        concat.addArgument(stateVariable.builder.irGetValue(ruiClass.builder.irThisReceiver()))
        concat.addArgument(irString(" ⇢ "))
        concat.addArgument(irGet(newValue))

        + ruiClass.builder.irPrintln(concat)

        return irGet(newValue)
    }

}
