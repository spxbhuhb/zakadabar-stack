/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.model.RuiExternalStateVariable
import zakadabar.rui.kotlin.plugin.model.RuiInternalStateVariable
import zakadabar.rui.kotlin.plugin.model.RuiStateVariable

class RuiStateVariableBuilder(
    override val ruiContext: RuiPluginContext,
    val ruiClassBuilder: RuiClassBuilder,
    val ruiStateVariable: RuiStateVariable
) : RuiBuilder {

    val irClass
        get() = ruiClassBuilder.irClass

    lateinit var field: IrField
    lateinit var irProperty: IrProperty
    lateinit var getter: IrSimpleFunction
    lateinit var setter: IrSimpleFunction

    init {
        when (ruiStateVariable) {
            is RuiInternalStateVariable -> build(ruiStateVariable.irVariable)
            is RuiExternalStateVariable -> build(ruiStateVariable.irValueParameter)
        }
    }

    fun build(irValueParameter: IrValueParameter) {
        buildField(irValueParameter.type)
        buildProperty()

        val constructorParameter = ruiClassBuilder.constructor.addValueParameter {
            name = irValueParameter.name
            type = irValueParameter.type
            varargElementType = irValueParameter.varargElementType
        }

        field.initializer = irFactory
            .createExpressionBody(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                IrGetValueImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, constructorParameter.symbol, IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER
                )
            )
    }

    fun build(irVariable: IrVariable) {
        buildField(irVariable.type)
        buildProperty()

        irVariable.initializer?.let { initializer ->
            field.initializer = irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, initializer)
        }
    }

    fun buildField(irType: IrType) {
        field = irFactory.buildField {
            name = Name.identifier(ruiStateVariable.name)
            type = irType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
        }
    }

    fun buildProperty() {
        irProperty = irClass.addProperty {
            name = Name.identifier(ruiStateVariable.name)
            isVar = true
        }.apply {
            parent = irClass
            backingField = field
            addDefaultGetter(irClass, irBuiltIns)
        }

        addDefaultSetter()

        getter = irProperty.getter !!
        setter = irProperty.setter !!
    }

    fun addDefaultSetter() {

        irProperty.setter = irFactory.buildFun {

            origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
            name = Name.identifier("set-" + field.name.identifier)
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.FINAL
            returnType = irBuiltIns.unitType

        }.apply {

            parent = irProperty.parent
            correspondingPropertySymbol = irProperty.symbol

            val receiver = addDispatchReceiver {
                type = irProperty.parentAsClass.defaultType
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

    fun irGetValue(): IrCall {
        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            field.type,
            getter.symbol,
            0, 0
        ).apply {
            dispatchReceiver = irGetReceiver()
        }
    }

    fun irSetValue(value: IrExpression): IrCall {
        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            field.type,
            setter.symbol,
            0, 1
        ).apply {
            dispatchReceiver = irGetReceiver()
            putValueArgument(0, value)
        }
    }

    fun irGetReceiver() = IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irClass.thisReceiver !!.symbol)

}