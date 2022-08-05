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
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.model.RuiClass

open class RuiPropertyBuilder(
    override val ruiClass: RuiClass,
    val name: Name,
    val type: IrType,
    val isVar : Boolean = true
) : RuiBuilder {

    lateinit var irField: IrField
    lateinit var irProperty: IrProperty
    lateinit var getter: IrSimpleFunction
    lateinit var setter: IrSimpleFunction

    init {
        initField()
        initProperty()
    }

    fun initField() {
        irField = irFactory.buildField {
            name = this@RuiPropertyBuilder.name
            type = this@RuiPropertyBuilder.type
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
        }
    }

    fun initProperty() {
        irProperty = irClass.addProperty {
            name = this@RuiPropertyBuilder.name
            isVar = true
        }.apply {
            parent = irClass
            backingField = irField
            addDefaultGetter(irClass, irBuiltIns)
        }

        getter = irProperty.getter !!

        if (isVar) {
            addDefaultSetter()
            setter = irProperty.setter !!
        }
    }

    fun addDefaultSetter() {

        irProperty.setter = irFactory.buildFun {

            origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
            name = Name.identifier("set-" + irField.name.identifier)
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
                type = irField.type
            }

            body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {
                + irSetField(
                    receiver = irGet(receiver),
                    field = irField,
                    value = irGet(value)
                )
            }
        }
    }

    fun irGetValue(receiver : IrExpression = irThisReceiver()): IrCall {
        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irField.type,
            getter.symbol,
            0, 0,
            origin = IrStatementOrigin.GET_PROPERTY
        ).apply {
            dispatchReceiver = receiver
        }
    }

    fun irSetValue(value: IrExpression, receiver : IrExpression = irThisReceiver()): IrCall {
        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irField.type,
            setter.symbol,
            0, 1
        ).apply {
            dispatchReceiver = receiver
            putValueArgument(0, value)
        }
    }

}