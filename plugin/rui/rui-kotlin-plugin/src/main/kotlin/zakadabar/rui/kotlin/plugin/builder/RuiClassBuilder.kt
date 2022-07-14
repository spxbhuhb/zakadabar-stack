/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import org.jetbrains.kotlin.backend.common.deepCopyWithVariables
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
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
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.data.RuiFunction
import zakadabar.rui.kotlin.plugin.data.RuiVariable

class RuiClassBuilder(
    private val context: IrPluginContext,
    configuration: RuiPluginContext
) {

    val data = configuration.controlData

    val unitType = context.irBuiltIns.unitType

    val irFactory = IrFactoryImpl

    val ruiComponentClass = context.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "core", "ReactiveComponent"))) !!
    val ruiComponentType = ruiComponentClass.typeWith()

    fun buildRuiClass(file: IrFile, rFunction: RuiFunction): IrClass {
        return IrFactoryImpl.buildClass {

            kind = ClassKind.CLASS
            name = Name.identifier(rFunction.className)

        }.apply {

            parent = file
            superTypes = listOf(ruiComponentType)

            declareThisReceiverParameter(
                thisType = IrSimpleTypeImpl(symbol, false, emptyList(), emptyList()),
                thisOrigin = IrDeclarationOrigin.INSTANCE_RECEIVER
            )

            addConstructor()

            addVariables(rFunction)

            addInitializer(rFunction)

            addFakeOverrides(IrTypeSystemContextImpl(context.irBuiltIns))
        }
    }

    internal fun IrClass.addConstructor() {
        val clazz = this
        addConstructor {
            isPrimary = true
            returnType = clazz.typeWith()
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
                    clazz.symbol,
                    unitType
                )
            }
        }
    }

    internal fun IrClass.addInitializer(rFunction: RuiFunction) {
        declarations += irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        ).apply {
            parent = this@addInitializer

            body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

                rFunction.variables.forEach { (_, rVariable) ->
                    addVariableInitializer(this@addInitializer, rVariable)
                }
                
            }
        }
    }

    private fun IrBlockBody.addVariableInitializer(irClass: IrClass, rVariable: RuiVariable) {
        val irVariable = rVariable.irVariable

        statements += IrCallImpl.fromSymbolOwner(
            irVariable.startOffset, irVariable.endOffset, rVariable.property.setter!!.symbol
        ).apply {
            dispatchReceiver = IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irClass.thisReceiver!!.symbol)
            putValueArgument(0, irVariable.initializer!!.deepCopyWithVariables())
        }
    }

    internal fun IrClass.declareThisReceiverParameter(
        thisType: IrType,
        thisOrigin: IrDeclarationOrigin,
        startOffset: Int = this.startOffset,
        endOffset: Int = this.endOffset,
        name: Name = SpecialNames.THIS
    ) {
        thisReceiver = irFactory.createValueParameter(
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

    internal fun IrClass.addVariables(rFunction: RuiFunction) {
        rFunction.variables.forEach {
            addVariable(this, it.value)
        }
    }

    internal fun addVariable(irClass: IrClass, rVariable: RuiVariable) {
        val irVariable = rVariable.irVariable

        rVariable.field = irFactory.buildField {
            name = irVariable.name
            type = irVariable.type
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
        }

        rVariable.property = irClass.addProperty {
            name = irVariable.name
            isVar = true
        }.apply {
            backingField = rVariable.field
            addDefaultGetter(irClass, context.irBuiltIns)
            addDefaultSetter(rVariable.field)
        }

    }

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

            body = DeclarationIrBuilder(context, this.symbol).irBlockBody {
                + irSetField(
                    receiver = irGet(receiver),
                    field = field,
                    value = irGet(value)
                )
            }
        }
    }

}
