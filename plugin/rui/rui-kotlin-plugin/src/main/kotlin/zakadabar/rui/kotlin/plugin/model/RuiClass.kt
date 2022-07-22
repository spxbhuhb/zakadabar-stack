/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.util.statements
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.transform.builders.RuiClassBuilder
import zakadabar.rui.kotlin.plugin.transform.fromir.RuiBoundaryVisitor
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiClass(
    val ruiContext: RuiPluginContext,
    val irFunction: IrFunction,
) : RuiElement {

    val boundary = RuiBoundaryVisitor(ruiContext).findBoundary(irFunction)

    val originalStatements = checkNotNull(irFunction.body?.statements) { "missing function body" }

    val initializerStatements = mutableListOf<IrStatement>()
    val renderingStatements = mutableListOf<IrStatement>()

    val stateVariables = mutableMapOf<String, RuiStateVariable>()
    val dirtyMasks = mutableListOf<RuiDirtyMask>()

    lateinit var rootBlock: RuiBlock

    val symbolMap = mutableMapOf<IrSymbol, RuiElement>()

    val builder = RuiClassBuilder(ruiContext, this)

    val irClass
        get() = builder.irClass

    internal fun stateVariableByGetterOrNull(symbol: IrSymbol): RuiStateVariable? =
        symbolMap[symbol]?.let {
            val b1 = (it is RuiStateVariable)
            it as RuiStateVariable
            val b2 = (it.builder.getter.symbol == symbol)
            if (b1 && b2) it else null
        }

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        stateVariables.values.forEach { it.accept(visitor, data) }
        dirtyMasks.forEach { it.accept(visitor, data) }
        rootBlock.accept(visitor, data)
    }
}