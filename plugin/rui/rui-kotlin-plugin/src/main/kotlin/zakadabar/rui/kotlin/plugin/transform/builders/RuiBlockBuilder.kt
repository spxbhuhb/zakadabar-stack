/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.model.RuiBlock
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.*

class RuiBlockBuilder(
    override val ruiClass: RuiClass,
    val ruiBlock: RuiBlock
) : RuiFragmentBuilder {

    // we have to initialize this in build, after all other classes in the module are registered
    override lateinit var symbolMap: RuiClassSymbols

    override lateinit var propertyBuilder: RuiPropertyBuilder

    override fun build() {
        symbolMap = ruiContext.ruiSymbolMap.getSymbolMap(RUI_BLOCK_CLASS)

        if (! symbolMap.valid) {
            ErrorsRui.RUI_IR_INVALID_EXTERNAL_CLASS.report(ruiClass, ruiBlock.irBlock)
            return
        }

        ruiBlock.statements.forEach {
            (it.builder as RuiFragmentBuilder).build()
        }

        propertyBuilder = RuiPropertyBuilder(ruiClass, Name.identifier(ruiBlock.name), symbolMap.defaultType, isVar = false)

        buildAndAddInitializer()
    }

    fun buildAndAddInitializer() {
        if (! symbolMap.valid) return

        val value = IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            symbolMap.defaultType,
            symbolMap.primaryConstructor.symbol,
            0, 0,
            RUI_BLOCK_ARGUMENT_COUNT // adapter, array of fragments
        ).also { constructorCall ->

            constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER, irGet(ruiClassBuilder.adapter))
            constructorCall.putValueArgument(RUI_BLOCK_ARGUMENT_INDEX_FRAGMENTS, buildFragmentVarArg())

        }

        ruiClassBuilder.initializer.body.statements += IrSetFieldImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            propertyBuilder.irField.symbol,
            receiver = ruiClassBuilder.irThisReceiver(),
            value = value,
            irBuiltIns.unitType
        )
    }

    fun buildFragmentVarArg(): IrExpression {
        return IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(ruiContext.ruiFragmentType),
            ruiContext.ruiFragmentType,
        ).also { vararg ->
            ruiBlock.statements.forEach {
                val builder = it.builder as RuiFragmentBuilder
                vararg.addElement(builder.propertyBuilder.irGetValue(irThisReceiver()))
            }
        }
    }
}