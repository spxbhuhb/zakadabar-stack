/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.name.FqName
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_INVALID_EXTERNAL_CLASS
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException
import zakadabar.rui.kotlin.plugin.util.isSynthetic

class RuiSymbolMap(
    val ruiContext: RuiPluginContext
) {

    private val classSymbolMaps = mutableMapOf<FqName, RuiClassSymbols>()

    private val invalid = RuiClassSymbols(ruiContext.ruiFragmentClass.owner, false)

    fun getSymbolMap(fqName: FqName): RuiClassSymbols =
        classSymbolMaps[fqName] ?: loadClass(fqName)

    private fun loadClass(fqName: FqName): RuiClassSymbols {
        val irClass = try {
            ruiContext.ruiClasses[fqName]?.irClass
                ?: ruiContext.irContext.referenceClass(fqName)?.owner
        } catch (ex: RuiCompilationException) {
            return invalid
        }

        if (irClass == null) return invalid

        return RuiClassSymbols(irClass).also {
            classSymbolMaps[fqName] = it
        }
    }

}

/**
 * Helps lookup of state variables and functions in Rui classes.
 *
 * @property  stateVariables  List of external state variables. The list contains the state variables in order
 *                            they are declared in the original function. So, parameter position may be used
 *                            to index it.
 *
 */
class RuiClassSymbols(
    val irClass: IrClass,
    var valid: Boolean = true
) {
    private val stateVariables = mutableListOf<RuiStateVariableSymbol>()

    private val invalidateFunctions: List<IrSimpleFunctionSymbol>

    lateinit var create: IrSimpleFunction
    lateinit var patchRender: IrSimpleFunction
    lateinit var dispose: IrSimpleFunction

    val defaultType
        get() = irClass.defaultType

    val primaryConstructor
        get() = irClass.primaryConstructor ?: throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS)

    init {

        val constructor = irClass.primaryConstructor ?: throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS)

        val indices = constructor.valueParameters.map { it.name.identifier }

        val invalidate = mutableListOf<IrSimpleFunction>()

        irClass.declarations.forEach {
            when {
                it is IrSimpleFunction -> function(invalidate, it)
                it is IrProperty && ! it.name.isSynthetic() -> property(indices, it)
            }
        }

        invalidateFunctions = invalidate.sortedBy { it.name }.map { it.symbol }

        if (! ::create.isInitialized || ! ::patchRender.isInitialized || ! ::dispose.isInitialized) {
            throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS)
        }
    }

    private fun function(invalidate: MutableList<IrSimpleFunction>, it: IrSimpleFunction) {
        if (it.name.identifier.startsWith(RUI_INVALIDATE)) {
            invalidate += it
            return
        }

        when (it.name.identifier) {
            RUI_CREATE -> create = it
            RUI_PATCH -> patchRender = it
            RUI_DISPOSE -> dispose = it
        }
    }

    private fun property(indices: List<String>, it: IrProperty) {
        val name = it.name.identifier
        val index = indices.indexOf(name) - RUI_FRAGMENT_ARGUMENT_COUNT

        if (index >= 0) {
            stateVariables += RuiStateVariableSymbol(index, it)
            return
        }
    }

    fun setterFor(index: Int): IrSimpleFunctionSymbol =
        getStateVariable(index).property.setter?.symbol ?: throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS)

    fun getStateVariable(index: Int): RuiStateVariableSymbol =
        if (stateVariables.size > index) {
            stateVariables[index]
        } else {
            throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS)
        }

    fun getInvalidate(index: Int): IrFunctionSymbol =
        if (invalidateFunctions.size > index) {
            invalidateFunctions[index]
        } else {
            throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS)
        }

}

class RuiStateVariableSymbol(
    val index: Int,
    val property: IrProperty
)
