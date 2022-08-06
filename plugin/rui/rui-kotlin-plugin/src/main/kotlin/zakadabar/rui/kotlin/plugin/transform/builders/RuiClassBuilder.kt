/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.ir.addFakeOverrides
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.*
import zakadabar.rui.kotlin.plugin.util.toRuiClassFqName

class RuiClassBuilder(
    override val ruiClass: RuiClass
) : RuiBuilder {

    override val ruiClassBuilder: RuiClassBuilder
        get() = this

    override val ruiContext: RuiPluginContext
        get() = ruiClass.ruiContext

    val irFunction = ruiClass.irFunction

    val name: String
    val fqName: FqName

    val constructor: IrConstructor
    val initializer: IrAnonymousInitializer
    val thisReceiver: IrValueParameter

    lateinit var adapter: IrValueParameter

    val create: IrSimpleFunction
    val patchRender: IrSimpleFunction
    val dispose: IrSimpleFunction

    override val irClass: IrClass = irFactory.buildClass {
        startOffset = irFunction.startOffset
        endOffset = irFunction.endOffset
        origin = IrDeclarationOrigin.DEFINED
        name = irFunction.toRuiClassFqName().shortName()
        kind = ClassKind.CLASS
        visibility = irFunction.visibility
        modality = Modality.OPEN
    }

    init {

        irClass.parent = irFunction.file
        irClass.superTypes = listOf(ruiContext.ruiFragmentType)
        irClass.metadata = irFunction.metadata

        name = irClass.name.identifier
        fqName = irClass.kotlinFqName

        thisReceiver = initThisReceiver()
        constructor = initConstructor()
        initializer = initInitializer()

        create = initRuiFunction(RUI_CREATE, ruiContext.ruiCreate)
        patchRender = initRuiFunction(RUI_PATCH, ruiContext.ruiPatchRender)
        dispose = initRuiFunction(RUI_DISPOSE, ruiContext.ruiDispose)

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))
    }

    private fun initThisReceiver(): IrValueParameter {

        val thisReceiver = irFactory.createValueParameter(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            IrDeclarationOrigin.INSTANCE_RECEIVER,
            IrValueParameterSymbolImpl(),
            SpecialNames.THIS,
            UNDEFINED_PARAMETER_INDEX,
            IrSimpleTypeImpl(irClass.symbol, false, emptyList(), emptyList()),
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false
        ).also {
            it.parent = irClass
            irClass.thisReceiver = it
        }

        return thisReceiver
    }

    /**
     * Creates a primary constructor with a standard body (super class constructor call
     * and initializer call).
     *
     * Adds value parameters to the constructor:
     *
     * - `ruiAdapter` with type `RuiAdapter`
     * - `ruiExternalPatch` with type `(it : RuiFragment) -> Unit`
     *
     * Later, `RuiStateTransformer` adds parameters from the original function.
     */
    private fun initConstructor(): IrConstructor {

        val constructor = irClass.addConstructor {
            isPrimary = true
            returnType = irClass.typeWith()
        }

        adapter = constructor.addValueParameter {
            name = Name.identifier(RUI_ADAPTER)
            type = ruiContext.ruiAdapterType
        }

        val ruiExternalPatch = constructor.addValueParameter {
            name = Name.identifier(RUI_EXTERNAL_PATCH)
            type = ruiContext.ruiExternalPatchType
        }

        constructor.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                ruiContext.ruiFragmentType,
                ruiContext.ruiFragmentClass.constructors.first(),
                typeArgumentsCount = 0,
                valueArgumentsCount = RUI_FRAGMENT_ARGUMENT_COUNT
            ).also {
                it.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER, irGet(adapter))
                it.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH, irGet(ruiExternalPatch))
            }

            statements += IrInstanceInitializerCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                irClass.symbol,
                irBuiltIns.unitType
            )
        }

        return constructor
    }

    private fun initInitializer(): IrAnonymousInitializer {

        val initializer = irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        )

        initializer.parent = irClass
        initializer.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        // State initialisation must precede fragment initialisation, so the fragments
        // will get initialized state variable values in their constructor.
        // Individual fragment builders will append their own initialisation code
        // after the state is properly initialized.

        initializer.body.statements += ruiClass.initializerStatements

        return initializer
    }

    private fun initRuiFunction(functionName: String, overrides: IrSimpleFunctionSymbol): IrSimpleFunction =
        irFactory.buildFun {
            name = Name.identifier(functionName)
            returnType = irBuiltIns.unitType
            modality = Modality.OPEN
        }.also { function ->

            function.overriddenSymbols = listOf(overrides)
            function.parent = irClass

            function.addDispatchReceiver {
                type = irClass.defaultType
            }

            irClass.declarations += function
        }

    fun build() {

        ruiClass.dirtyMasks.forEach {
            it.builder.build()
        }

        val rootBuilder = ruiClass.rootBlock.builder as RuiFragmentBuilder
        rootBuilder.build()

        buildRuiCall(create, rootBuilder, rootBuilder.symbolMap.create)
        buildRuiCall(patchRender, rootBuilder, rootBuilder.symbolMap.patchRender)
        buildRuiCall(dispose, rootBuilder, rootBuilder.symbolMap.dispose)

        // The initializer has to be the last, so it will be able to access all properties
        irClass.declarations += initializer
    }

    fun buildRuiCall(function: IrSimpleFunction, rootBuilder: RuiFragmentBuilder, callee: IrSimpleFunction) {
        function.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {
            statements += irCall(
                callee.symbol,
                dispatchReceiver = rootBuilder.propertyBuilder.irGetValue(receiver = function.irGetReceiver())
            )
        }
    }
}