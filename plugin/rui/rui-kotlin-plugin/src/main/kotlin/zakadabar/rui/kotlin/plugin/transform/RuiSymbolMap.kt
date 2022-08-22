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
import zakadabar.rui.kotlin.plugin.*
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_INVALID_EXTERNAL_CLASS
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException
import zakadabar.rui.kotlin.plugin.util.isSynthetic

class RuiSymbolMap(
    val ruiContext: RuiPluginContext
) {

    private val classSymbolMaps = mutableMapOf<FqName, RuiClassSymbols>()

    fun getSymbolMap(fqName: FqName): RuiClassSymbols =
        classSymbolMaps[fqName] ?: loadClass(fqName)

    private fun loadClass(fqName: FqName): RuiClassSymbols {
        val irClass =
            ruiContext.ruiClasses[fqName]?.irClass
                ?: ruiContext.irContext.referenceClass(fqName)?.owner
                ?: throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing class: ${fqName.asString()}")

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

    lateinit var externalPatch: IrProperty
    lateinit var externalPatchGetter: IrSimpleFunction

    lateinit var create: IrSimpleFunction
    lateinit var mount: IrSimpleFunction
    lateinit var patch: IrSimpleFunction
    lateinit var dispose: IrSimpleFunction
    lateinit var unmount: IrSimpleFunction

    val defaultType
        get() = irClass.defaultType

    val primaryConstructor
        get() = irClass.primaryConstructor ?: throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing primary constructor")

    init {

        val indices = primaryConstructor.valueParameters.map { it.name.identifier }

        val invalidate = mutableListOf<IrSimpleFunction>()

        irClass.declarations.forEach {
            when {
                it is IrSimpleFunction -> function(invalidate, it)
                it is IrProperty && ! it.name.isSynthetic() -> property(indices, it)
            }
        }

        invalidateFunctions = invalidate.sortedBy { it.name }.map { it.symbol }

        stateVariables.sortBy { it.index }

        if (! ::externalPatch.isInitialized) {
            throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing $RUI_EXTERNAL_PATCH property")
        }

        if (
            ! ::create.isInitialized ||
            ! ::mount.isInitialized ||
            ! ::patch.isInitialized ||
            ! ::mount.isInitialized ||
            ! ::dispose.isInitialized
        ) {
            throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing default Rui method(s)")
        }
    }

    private fun function(invalidate: MutableList<IrSimpleFunction>, it: IrSimpleFunction) {
        if (it.name.identifier.startsWith(RUI_INVALIDATE)) {
            invalidate += it
            return
        }

        when (it.name.identifier) {
            RUI_CREATE -> create = it
            RUI_MOUNT -> mount = it
            RUI_PATCH -> patch = it
            RUI_UNMOUNT -> unmount = it
            RUI_DISPOSE -> dispose = it
        }
    }

    private fun property(indices: List<String>, it: IrProperty) {
        val name = it.name.identifier
        val index = indices.indexOf(name) - RUI_FRAGMENT_ARGUMENT_COUNT

        if (name == RUI_EXTERNAL_PATCH) {
            externalPatch = it
            externalPatchGetter = it.getter ?: throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing $RUI_EXTERNAL_PATCH getter")
            return
        }

        if (index >= 0) {
            stateVariables += RuiStateVariableSymbol(index, it)
            return
        }
    }

    fun setterFor(index: Int): IrSimpleFunctionSymbol =
        getStateVariable(index).property.setter?.symbol
            ?: throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "no setter for $index")

    fun getStateVariable(index: Int): RuiStateVariableSymbol =
        if (stateVariables.size > index) {
            stateVariables[index]
        } else {
            throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "no state variable for $index")
        }

    fun getInvalidate(index: Int): IrFunctionSymbol =
        if (invalidateFunctions.size > index) {
            invalidateFunctions[index]
        } else {
            throw RuiCompilationException(RUI_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "no state variable for $index")
        }

}

class RuiStateVariableSymbol(
    val index: Int,
    val property: IrProperty
)
