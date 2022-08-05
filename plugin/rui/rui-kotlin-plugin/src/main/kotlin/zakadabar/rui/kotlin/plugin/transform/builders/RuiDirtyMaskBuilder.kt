/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.addDispatchReceiver
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.model.RuiDirtyMask
import zakadabar.rui.kotlin.plugin.transform.RUI_INVALIDATE
import zakadabar.rui.kotlin.plugin.transform.RUI_MASK

class RuiDirtyMaskBuilder(
    override val ruiClass: RuiClass,
    val ruiDirtyMask: RuiDirtyMask
) : RuiBuilder {

    val propertyBuilder = RuiPropertyBuilder(ruiClass, ruiDirtyMask.name, irBuiltIns.intType)

    fun build() {
        buildInitializer()
        buildInvalidate()
    }

    fun buildInitializer() {
        propertyBuilder.irField.initializer = irFactory.createExpressionBody(irConst(0))
    }

    fun buildInvalidate() {
        irFactory.buildFun {
            name = Name.identifier(RUI_INVALIDATE + ruiDirtyMask.index)
            returnType = irBuiltIns.unitType
            modality = Modality.OPEN
        }.also { function ->

            function.parent = irClass

            val receiver = function.addDispatchReceiver {
                type = irClass.defaultType
            }

            val mask = function.addValueParameter {
                name = Name.identifier(RUI_MASK)
                type = irBuiltIns.intType
            }

            function.body = buildInvalidateBody(function, receiver, mask)

            irClass.declarations += function
        }
    }

    private fun buildInvalidateBody(function: IrSimpleFunction, receiver: IrValueParameter, mask: IrValueParameter): IrBody =
        DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            + propertyBuilder.irSetValue(
                irOr(
                    propertyBuilder.irGetValue(irGet(receiver)),
                    irGet(mask)
                ),
                receiver = irGet(receiver)
            )
        }
}