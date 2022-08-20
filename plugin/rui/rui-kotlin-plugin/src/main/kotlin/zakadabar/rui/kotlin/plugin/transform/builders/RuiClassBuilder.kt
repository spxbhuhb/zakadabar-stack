/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.IrTypeParameterImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrTypeParameterSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.types.Variance
import zakadabar.rui.kotlin.plugin.RuiDumpPoint
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.*
import zakadabar.rui.kotlin.plugin.util.traceLabel

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

    private lateinit var adapterConstructorParameter: IrValueParameter
    private lateinit var externalPatchConstructorParameter: IrValueParameter

    val adapterPropertyBuilder: RuiPropertyBuilder
    val externalPatchPropertyBuilder: RuiPropertyBuilder

    val create: IrSimpleFunction
    val mount: IrSimpleFunction
    val patch: IrSimpleFunction
    val unmount: IrSimpleFunction
    val dispose: IrSimpleFunction

    override val irClass: IrClass = irFactory.buildClass {
        startOffset = irFunction.startOffset
        endOffset = irFunction.endOffset
        origin = IrDeclarationOrigin.DEFINED
        name = ruiClass.name
        kind = ClassKind.CLASS
        visibility = irFunction.visibility
        modality = Modality.OPEN
    }

    init {
        initTypeParameters()

        irClass.parent = irFunction.file
        irClass.superTypes = listOf(ruiContext.ruiFragmentClass.typeWith(irClass.typeParameters.first().defaultType))
        irClass.metadata = irFunction.metadata

        name = irClass.name.identifier
        fqName = irClass.kotlinFqName

        thisReceiver = initThisReceiver()
        constructor = initConstructor()

        adapterPropertyBuilder = initAdapterProperty()
        externalPatchPropertyBuilder = initExternalPatchProperty()

        initializer = initInitializer()

        create = initRuiFunction(RUI_CREATE, ruiContext.ruiCreate)
        mount = initRuiFunction(RUI_MOUNT, ruiContext.ruiMount)
        patch = initRuiFunction(RUI_PATCH, ruiContext.ruiPatch)
        dispose = initRuiFunction(RUI_DISPOSE, ruiContext.ruiDispose)
        unmount = initRuiFunction(RUI_UNMOUNT, ruiContext.ruiUnmount)
    }

    private fun initTypeParameters() {
        irClass.typeParameters = listOf(
            IrTypeParameterImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                IrDeclarationOrigin.BRIDGE_SPECIAL,
                IrTypeParameterSymbolImpl(),
                Name.identifier(RUI_BT),
                index = 0,
                isReified = false,
                variance = Variance.IN_VARIANCE,
                factory = irFactory
            ).also {
                it.parent = irClass
                it.superTypes = listOf(irBuiltIns.anyNType)
            }
        )
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

    private fun initAdapterProperty(): RuiPropertyBuilder =
        RuiPropertyBuilder(ruiClassBuilder, Name.identifier(RUI_ADAPTER), classBoundAdapterType, isVar = false).also {
            it.irField.initializer = irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irGet(adapterConstructorParameter))
        }

    private fun initExternalPatchProperty(): RuiPropertyBuilder =
        RuiPropertyBuilder(ruiClassBuilder, Name.identifier(RUI_EXTERNAL_PATCH), classBoundExternalPatchType, isVar = false).also {
            it.irField.initializer = irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irGet(externalPatchConstructorParameter))
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

        adapterConstructorParameter = constructor.addValueParameter {
            name = Name.identifier(RUI_ADAPTER)
            type = ruiContext.ruiAdapterType
        }

        externalPatchConstructorParameter = constructor.addValueParameter {
            name = Name.identifier(RUI_EXTERNAL_PATCH)
            type = classBoundExternalPatchType
        }

        constructor.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                irBuiltIns.anyType,
                irBuiltIns.anyClass.constructors.first(),
                typeArgumentsCount = 0,
                valueArgumentsCount = 0
            )

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

            if (functionName == RUI_MOUNT || functionName == RUI_UNMOUNT) {
                function.addValueParameter {
                    name = Name.identifier("bridge")
                    type = ruiContext.ruiBridgeType
                }
            }

            irClass.declarations += function
        }

    fun build() {

        traceInit()

        // State initialisation must precede fragment initialisation, so the fragments
        // will get initialized values in their constructor.
        // Individual fragment builders will append their own initialisation code
        // after the state is properly initialized.

        initializer.body.statements += ruiClass.initializerStatements

        val rootBuilder = ruiClass.rootBlock.builder as RuiFragmentBuilder
        rootBuilder.build()

        buildRuiCall(create, rootBuilder, rootBuilder.symbolMap.create)
        buildRuiCall(mount, rootBuilder, rootBuilder.symbolMap.mount)
        buildRuiCall(patch, rootBuilder, rootBuilder.symbolMap.patch)
        buildRuiCall(unmount, rootBuilder, rootBuilder.symbolMap.unmount)
        buildRuiCall(dispose, rootBuilder, rootBuilder.symbolMap.dispose)

        // The initializer has to be the last, so it will be able to access all properties
        irClass.declarations += initializer

        RuiDumpPoint.KotlinLike.dump(ruiContext) {
            println(irClass.dumpKotlinLike())
        }
    }

    fun traceInit() {
        if (! ruiContext.withTrace) return
        initializer.body.statements += irPrintln(traceLabel(ruiClass.name, "init"))
    }

    fun buildRuiCall(function: IrSimpleFunction, rootBuilder: RuiFragmentBuilder, callee: IrSimpleFunction) {
        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            traceRuiCall(function)

            val symbolMap = rootBuilder.symbolMap

            when (callee.name.identifier) {
                RUI_CREATE -> rootBuilder.irRuiCall(symbolMap.create, function, this)
                RUI_MOUNT -> rootBuilder.irRuiCallWithBridge(symbolMap.mount, function, this)
                RUI_PATCH -> {
                    rootBuilder.irCallExternalPatch(function, this)
                    rootBuilder.irRuiCall(symbolMap.patch, function, this)
                }
                RUI_UNMOUNT -> rootBuilder.irRuiCallWithBridge(symbolMap.unmount, function, this)
                RUI_DISPOSE -> rootBuilder.irRuiCall(symbolMap.dispose, function, this)
            }
        }
    }

    private fun IrBlockBodyBuilder.traceRuiCall(function: IrSimpleFunction) {

        if (! ruiContext.withTrace) return

        val name = function.name.identifier

        + when {
            name.startsWith("ruiPatch") -> {
                val concat = irConcat()
                concat.addArgument(irString(traceLabel(ruiClass.name, "patch")))

                ruiClass.dirtyMasks.forEach {
                    concat.addArgument(irString(" ${it.name}: "))
                    concat.addArgument(it.builder.propertyBuilder.irGetValue(irGet(function.dispatchReceiverParameter !!)))
                }

                irPrintln(concat)
            }
            else -> irPrintln(traceLabel(ruiClass.name, function.name.identifier.substring(3).lowercase()))
        }

    }

}