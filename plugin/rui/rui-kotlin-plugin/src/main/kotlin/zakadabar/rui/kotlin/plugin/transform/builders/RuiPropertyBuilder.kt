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
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.Name

open class RuiPropertyBuilder(
    override val ruiClassBuilder: RuiClassBuilder
) : RuiBuilder {

    lateinit var name : Name
    lateinit var field: IrField
    lateinit var irProperty: IrProperty
    lateinit var getter: IrSimpleFunction
    lateinit var setter: IrSimpleFunction

    constructor(name : String, ruiClassBuilder: RuiClassBuilder) : this(ruiClassBuilder) {
        this.name = Name.identifier(name)
    }

    fun buildField(irType: IrType) {
        field = irFactory.buildField {
            name = this@RuiPropertyBuilder.name
            type = irType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
        }
    }

    fun buildProperty() {
        irProperty = irClass.addProperty {
            name = this@RuiPropertyBuilder.name
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