/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.backend.common.ir.addFakeOverrides
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.IrFactoryImpl
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import zakadabar.rui.kotlin.plugin.RuiPluginContext

/**
 * Represents the building process of one Rui class.
 */
class RuiClassBuilder(
    private val ruiContext: RuiPluginContext,
    val original: IrFunction,
    val boundary: Int
) {

    val irContext = ruiContext.irPluginContext
    val irFactory = irContext.irFactory

    val unitType = irContext.irBuiltIns.unitType

    val ruiComponentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "core", "RuiComponentBase"))) !!
    val ruiComponentType = ruiComponentClass.typeWith()

    lateinit var irClass: IrClass
    lateinit var constructor: IrConstructor
    lateinit var initializer: IrAnonymousInitializer
    lateinit var invalidate: IrSimpleFunctionSymbol

    /**
     * Variables that define the state of the component. These come from two sources:
     * parameters and top-level variables of the original function. Each variable
     * has an `index` that is the bit number in `dirty`.
     *
     * Key of the map is the property identifier.
     *
     * To add a property, use [addStateVariable], to get one use [getStateVariable].
     */
    private val stateVariables = mutableMapOf<String, RuiStateVariable>()

    class RuiStateVariable(
        val irProperty: IrProperty,
        val index: Int
    )

    fun build(builders: RuiClassBuilder.() -> Unit): IrClass {
        return IrFactoryImpl.buildClass {

            kind = ClassKind.CLASS
            name = Name.identifier("Rui" + original.name.identifier.capitalizeAsciiOnly())

        }.apply {

            irClass = this
            parent = original.file
            superTypes = listOf(ruiComponentType)

            declareThisReceiverParameter(
                thisType = IrSimpleTypeImpl(symbol, false, emptyList(), emptyList()),
                thisOrigin = IrDeclarationOrigin.INSTANCE_RECEIVER
            )

            declareConstructor()

            declareInitializer()

            irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

            getInvalidate()

            builders()
        }
    }

    internal fun getInvalidate() {
        invalidate = irClass.declarations
            .first { it is IrOverridableMember && it.name.identifier == "invalidate" }
            .symbol as IrSimpleFunctionSymbol
    }

    internal fun declareConstructor() {

        constructor =

            irClass.addConstructor {
                isPrimary = true
                returnType = irClass.typeWith()
            }.apply {

                body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

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

            }
    }

    internal fun declareInitializer() {

        initializer =

            irFactory.createAnonymousInitializer(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                origin = IrDeclarationOrigin.DEFINED,
                symbol = IrAnonymousInitializerSymbolImpl(),
                isStatic = false
            ).apply {
                parent = irClass
                irClass.declarations += this
                body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)
            }
    }

    internal fun IrClass.declareThisReceiverParameter(
        thisType: IrType,
        thisOrigin: IrDeclarationOrigin,
        startOffset: Int = this.startOffset,
        endOffset: Int = this.endOffset,
        name: Name = SpecialNames.THIS
    ) {

        thisReceiver =

            irFactory.createValueParameter(
                startOffset,
                endOffset,
                thisOrigin,
                IrValueParameterSymbolImpl(),
                name,
                UNDEFINED_PARAMETER_INDEX,
                thisType,
                varargElementType = null,
                isCrossinline = false,
                isNoinline = false,
                isHidden = false,
                isAssignable = false
            ).apply {
                this.parent = this@declareThisReceiverParameter
            }
    }

    internal fun addStateVariable(irVariable: IrVariable): IrProperty {

        val field = irFactory.buildField {
            name = irVariable.name
            type = irVariable.type
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
        }

        irClass.addProperty {
            name = irVariable.name
            isVar = true
        }.apply {
            parent = irClass
            backingField = field
            addDefaultGetter(irClass, irContext.irBuiltIns)
            addDefaultSetter(field)
            stateVariables[name.identifier] = RuiStateVariable(this, stateVariables.size)
            return this
        }

    }

    internal fun addStateVariable(irValueParameter: IrValueParameter) {

        val field = irFactory.buildField {
            name = irValueParameter.name
            type = irValueParameter.type
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
            initializer = irFactory.createExpressionBody(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                IrGetValueImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irValueParameter.symbol, IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER
                )
            )
        }

        irClass.addProperty {
            name = irValueParameter.name
            isVar = true
        }.apply {
            backingField = field
            addDefaultGetter(irClass, irContext.irBuiltIns)
            addDefaultSetter(field)
            stateVariables[name.identifier] = RuiStateVariable(this, stateVariables.size)
        }

    }

    internal fun getStateVariable(identifier: String) = stateVariables[identifier]

    internal fun IrProperty.addDefaultSetter(field: IrField) {
        val prop = this

        setter = irFactory.buildFun {

            origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
            name = Name.identifier("set-" + field.name.identifier)
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.FINAL
            returnType = unitType

        }.apply {

            parent = prop.parent
            correspondingPropertySymbol = prop.symbol

            val receiver = addDispatchReceiver {
                type = prop.parentAsClass.defaultType
            }

            val value = addValueParameter {
                name = Name.identifier("set-?")
                type = field.type
            }

            body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {
                + irSetField(
                    receiver = irGet(receiver),
                    field = field,
                    value = irGet(value)
                )
            }
        }
    }

}
