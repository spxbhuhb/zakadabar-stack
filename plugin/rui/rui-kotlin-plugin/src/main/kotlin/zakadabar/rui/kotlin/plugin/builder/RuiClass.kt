/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.backend.common.ir.addFakeOverrides
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.lower.RuiBoundaryVisitor
import zakadabar.rui.kotlin.plugin.lower.RuiRenderingTransformer
import zakadabar.rui.kotlin.plugin.lower.RuiStateVariableTransformer
import zakadabar.rui.kotlin.plugin.lower.toRuiClassName

/**
 * Represents the compilation of one Rui class.
 */
class RuiClass(
    val ruiContext: RuiPluginContext,
    val original: IrFunction
) {

    val originalStatements = checkNotNull(original.body?.statements) { "missing function body" }
    val boundary = RuiBoundaryVisitor(ruiContext).findBoundary(original)

    val name: String
    val fqName : FqName
    val irClass: IrClass
    val constructor: IrConstructor
    val initializer: IrAnonymousInitializer
    val thisReceiver: IrValueParameter
    val create: IrFunction
    val patch: IrFunction
    val dispose: IrFunction
    val invalidate: IrSimpleFunctionSymbol

    val stateVariables = mutableMapOf<String, RuiStateVariable>()
    val componentSlots = mutableMapOf<String, RuiComponentSlot>()

    val irContext = ruiContext.irPluginContext
    val irFactory = irContext.irFactory
    val irBuiltIns = irContext.irBuiltIns

    val unitType = irBuiltIns.unitType

    val ruiComponentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "core", "RuiComponentBase"))) !!
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
        create = buildRuiOverride("create")
        patch = buildRuiOverride("patch")
        dispose = buildRuiOverride("dispose")

        addPatchParameter()

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        invalidate = findInvalidate()

        transformStateDefinition()
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

        original.valueParameters.forEach { old ->
            constructor.addValueParameter {
                name = old.name
                type = old.type
                varargElementType = old.varargElementType
            }.also { new ->
                addStateVariable(new)
            }
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

    private fun buildRuiOverride(funName: String): IrFunction {
        return irFactory.addFunction(irClass) {
            name = Name.identifier(funName)
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.OPEN
            returnType = unitType
        }.apply {
            overriddenSymbols = listOf(ruiComponentClass.functionByName(funName))
            body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)
        }
    }

    private fun addPatchParameter() {
        patch.addValueParameter("mask", irBuiltIns.intArray.defaultType, IrDeclarationOrigin.DEFINED)
    }

    fun finalize() {
        // have to add initializer last, so it will be able to access all properties
        irClass.declarations += initializer
    }

    private fun findInvalidate(): IrSimpleFunctionSymbol {
        return irClass.declarations
            .first { it is IrOverridableMember && it.name.identifier == "invalidate" }
            .symbol as IrSimpleFunctionSymbol
    }

    internal fun addStateVariable(irVariable: IrVariable): RuiStateVariable {
        return RuiStateVariable(this, stateVariables.size, irVariable).also {
            stateVariables[it.originalName] = it
        }
    }

    internal fun addStateVariable(irValueParameter: IrValueParameter): RuiStateVariable {
        return RuiStateVariable(this, stateVariables.size, irValueParameter).also {
            stateVariables[it.originalName] = it
        }
    }

    internal fun getStateVariable(identifier: String) = stateVariables[identifier]

    internal fun addComponentSlot(irCall: IrCall): RuiComponentSlot {
        return RuiComponentSlot(this, componentSlots.size, irCall).also {
            componentSlots[it.name] = it
        }
    }

    fun transformStateDefinition() {
        RuiStateVariableTransformer(ruiContext, this).buildStateDefinition()
    }

    fun transformRendering() {
        RuiRenderingTransformer(ruiContext, this).buildRendering()
    }

    fun irGetReceiver() = IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, thisReceiver.symbol)

}