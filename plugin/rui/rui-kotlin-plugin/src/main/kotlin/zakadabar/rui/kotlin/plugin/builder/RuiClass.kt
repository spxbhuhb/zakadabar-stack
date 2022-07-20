/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.backend.common.ir.addFakeOverrides
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.UNDEFINED_PARAMETER_INDEX
import org.jetbrains.kotlin.ir.builders.declarations.addConstructor
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.SpecialNames
import zakadabar.rui.kotlin.plugin.RuiGenerationExtension
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.state.definition.RuiBoundaryVisitor
import zakadabar.rui.kotlin.plugin.state.definition.RuiFunctionVisitor
import zakadabar.rui.kotlin.plugin.state.definition.RuiStateVariableTransformer
import zakadabar.rui.kotlin.plugin.state.definition.toRuiClassName
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

/**
 * Represents the compilation of one Rui component. The source of the compilation
 * is the function annotated with `@Rui`. The result of the compilation is an IR
 * class that extends `RuiBlock`.
 *
 * [RuiClass] stores the all the information needed to compile the IR class:
 *
 * - [stateVariables]
 * - [initializer] (state initialization)
 * - [renderingSlots]
 * - [dirtyMasks]
 *
 * The compilation is a multiphase process.
 *
 * The "State Definition" phase:
 *
 * 1. [RuiGenerationExtension] runs a [RuiFunctionVisitor] on the module fragment
 * 1. [RuiFunctionVisitor] creates the [RuiClass] instance
 * 1. [RuiClass] finds the [boundary] between the state definition and rendering part
 * 1. `init` of [RuiClass] creates the [IrClass] and the basic class functions
 * 1. `init` of [RuiClass] runs a [RuiStateVariableTransformer] to build the state definition
 * 1. [RuiStateVariableTransformer] creates [stateVariables] for function parameters and top-level variable declarations
 * 1. [RuiStateVariableTransformer] transforms function parameter and top-level variable accesses/changes into state variable getter/setter calls
 * 1. during state variable processing [dirtyMasks] are created as needed
 * 1. [RuiFunctionVisitor] adds the [RuiClass] instance to [RuiPluginContext.ruiClasses]
 *
 * The "Rendering" phase:
 *
 * 1. [RuiGenerationExtension] calls [build] for each instance in [RuiPluginContext.ruiClasses]
 */
class RuiClass(
    val ruiContext: RuiPluginContext,
    val original: IrFunction
) : RuiElement {

    // data from the original function

    val originalStatements = checkNotNull(original.body?.statements) { "missing function body" }
    val boundary = RuiBoundaryVisitor(ruiContext).findBoundary(original)

    // the generated IR class

    val name: String
    val fqName: FqName
    val irClass: IrClass

    // the generated IR functions in the IR class

    val constructor: IrConstructor
    val initializer: IrAnonymousInitializer
    val thisReceiver: IrValueParameter

    // parts of the generated Rui component

    val stateVariables = mutableMapOf<String, RuiStateVariable>()
    val renderingSlots = mutableMapOf<String, RuiRenderingSlot>()
    val dirtyMasks = mutableListOf<RuiDirtyMask>()
    val blocks = mutableListOf<RuiStatement>()

    val symbolMap = mutableMapOf<IrSymbol, RuiElement>()

    lateinit var rootBlock : RuiBlock

    // IR shortcuts

    val irContext = ruiContext.irPluginContext
    val irFactory = irContext.irFactory
    val irBuiltIns = irContext.irBuiltIns

    val unitType = irBuiltIns.unitType

    // references to the Rui runtime

    val ruiComponentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiBlock"))) !!
    val ruiComponentType = ruiComponentClass.typeWith()

    init {
        irClass = irFactory.buildClass {

            startOffset = original.startOffset
            endOffset = original.endOffset
            origin = IrDeclarationOrigin.DEFINED
            name = original.name.toRuiClassName()
            kind = ClassKind.CLASS
            visibility = original.visibility
            modality = Modality.OPEN

        }

        irClass.parent = original.file
        irClass.superTypes = listOf(ruiComponentType)
        irClass.metadata = original.metadata

        name = irClass.name.identifier
        fqName = irClass.kotlinFqName

        thisReceiver = buildThisReceiver()
        constructor = buildConstructor()
        initializer = buildInitializer()

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        RuiStateVariableTransformer(ruiContext, this).buildStateDefinition()
    }

    private fun buildThisReceiver(): IrValueParameter {

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

    private fun buildConstructor(): IrConstructor {

        val constructor = irClass.addConstructor {
            isPrimary = true
            returnType = irClass.typeWith()
        }

        constructor.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                ruiComponentType,
                ruiComponentClass.constructors.first()
            )

            statements += IrInstanceInitializerCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                irClass.symbol,
                unitType
            )
        }

        return constructor
    }

    private fun buildInitializer(): IrAnonymousInitializer {

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

    fun build() {
        // The initializer has to be the last, so it will be able to access all properties
        irClass.declarations += initializer
    }

    internal fun addStateVariable(irVariable: IrVariable): RuiStateVariable =
        RuiStateVariable(this, stateVariables.size, irVariable).add()

    internal fun addStateVariable(irValueParameter: IrValueParameter): RuiStateVariable =
        RuiStateVariable(this, stateVariables.size, irValueParameter).add()

    private fun RuiStateVariable.add(): RuiStateVariable {
        this@RuiClass.stateVariables[originalName] = this
        this@RuiClass.symbolMap[getter.symbol] = this
        this@RuiClass.addDirtyMask(this)
        return this
    }

    /**
     * Adds a new [RuiDirtyMask] if there are not enough masks already for this
     * variable index.
     */
    fun addDirtyMask(ruiStateVariable: RuiStateVariable) {
        val maskNumber = ruiStateVariable.index / 32
        if (dirtyMasks.size > maskNumber) return
        dirtyMasks += RuiDirtyMask(maskNumber)
    }

    internal fun getStateVariable(identifier: String) = stateVariables[identifier]

    internal fun stateVariableByGetterOrNull(symbol: IrSymbol): RuiStateVariable? =
        symbolMap[symbol]?.let { if (it is RuiStateVariable && it.getter.symbol == symbol) it else null }

    fun irGetReceiver() = IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, thisReceiver.symbol)

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {
        stateVariables.values.forEach { it.accept(visitor, data) }
        renderingSlots.values.forEach { it.accept(visitor, data) }
        dirtyMasks.forEach { it.accept(visitor, data) }
        rootBlock.accept(visitor, data)
    }
}