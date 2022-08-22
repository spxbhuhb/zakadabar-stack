/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.*
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.model.RuiBranch
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.model.RuiWhen
import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException
import zakadabar.rui.runtime.Plugin.RUI_WHEN_CLASS

class RuiWhenBuilder(
    override val ruiClassBuilder: RuiClassBuilder,
    val ruiWhen: RuiWhen
) : RuiFragmentBuilder {

    // we have to initialize this in build, after all other classes in the module are registered
    override lateinit var symbolMap: RuiClassSymbols

    override lateinit var propertyBuilder: RuiPropertyBuilder

    override fun build() {
        symbolMap = ruiContext.ruiSymbolMap.getSymbolMap(RUI_FQN_WHEN_CLASS)

        if (! symbolMap.valid) {
            ErrorsRui.RUI_IR_INVALID_EXTERNAL_CLASS.report(ruiClass, ruiWhen.irWhen, additionalInfo = "invalid symbol map for $RUI_WHEN_CLASS")
            return
        }

        ruiWhen.branches.forEach {
            buildBranch(it)
        }

        propertyBuilder = RuiPropertyBuilder(ruiClassBuilder, Name.identifier(ruiWhen.name), symbolMap.defaultType, isVar = false)

        buildAndAddInitializer()
    }

    fun buildAndAddInitializer() {
        if (! symbolMap.valid) return

        val value = IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            symbolMap.defaultType,
            symbolMap.primaryConstructor.symbol,
            0, 0,
            RUI_WHEN_ARGUMENT_COUNT // adapter, select, array of fragments
        ).also { constructorCall ->

            constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER, ruiClassBuilder.adapterPropertyBuilder.irGetValue())
            constructorCall.putValueArgument(RUI_WHEN_ARGUMENT_INDEX_SELECT, buildSelect())
            constructorCall.putValueArgument(RUI_WHEN_ARGUMENT_INDEX_FRAGMENTS, buildFragmentVarArg())

        }

        ruiClassBuilder.initializer.body.statements += IrSetFieldImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            propertyBuilder.irField.symbol,
            receiver = ruiClassBuilder.irThisReceiver(),
            value = value,
            irBuiltIns.unitType
        )
    }

    fun buildSelect(): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = irBuiltIns.intType
        }.also { function ->

            function.parent = irClass
            function.visibility = DescriptorVisibilities.LOCAL

            function.body = buildSelectBody(function)
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundExternalPatchType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }

    private fun buildSelectBody(function: IrSimpleFunction): IrBody? {
        return try {

            DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
                + irReturn(
                    if (ruiWhen.irSubject == null) {
                        buildWhen()
                    } else {
                        TODO("when with subject")
                    }
                )
            }

        } catch (ex: RuiCompilationException) {
            ex.error.report(ruiClassBuilder, ruiWhen.irWhen, additionalInfo = ex.additionalInfo)
            DeclarationIrBuilder(irContext, function.symbol).irBlockBody { }
        }
    }

    fun IrBlockBodyBuilder.buildWhen(): IrExpression {
        val branches = ruiWhen.branches.mapIndexed { index, branch ->
            irBranch(branch.condition.irExpression, irConst(index))
        }.toMutableList()

        if (ruiWhen.irWhen.origin == IrStatementOrigin.IF && branches.size == 1) {
            branches += irBranch(irConst(true), irConst(- 1))
        }

        // add "else" to the when if the last condition is not a constant true
        if (ruiWhen.irWhen.origin == IrStatementOrigin.WHEN) {
            branches.last().condition.let {
                if (! (it is IrConst<*> && it.value is Boolean && it.value == true)) {
                    branches += irBranch(irConst(true), irConst(- 1))
                }
            }
        }

        return irWhen(
            irBuiltIns.intType,
            branches
        )
    }

    fun buildFragmentVarArg(): IrExpression {
        return IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(ruiContext.ruiFragmentType),
            ruiContext.ruiFragmentType,
        ).also { vararg ->
            ruiWhen.branches.forEach {
                val builder = it.result.builder as RuiFragmentBuilder
                vararg.addElement(builder.propertyBuilder.irGetValue(irThisReceiver()))
            }
        }
    }

    fun buildBranch(branch: RuiBranch) {

//            ruiEntryPoint.irFunction.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {
//
//                val instance = IrConstructorCallImpl(
//                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
//                    ruiClass.irClass.defaultType,
//                    ruiClass.builder.constructor.symbol,
//                    0, 0, 2
//                ).also { call ->
//                    call.putValueArgument(0, irGetAdapter(function))
//                    call.putValueArgument(1, buildExternalPatch(ruiClass, function.symbol))
//                }
//
//                val root = irTemporary(instance, "root").also { it.parent = ruiEntryPoint.irFunction }
//
//                statements += root
//
//                statements += irCall(
//                    ruiClass.builder.create.symbol,
//                    dispatchReceiver = irGet(root)
//                )
//
//                statements += irCall(
//                    ruiClass.builder.mount.symbol,
//                    dispatchReceiver = irGet(root),
//                    args = arrayOf(
//                        irCall(
//                            ruiContext.ruiAdapterClass.getPropertyGetter(RUI_ROOT_BRIDGE) !!.owner.symbol,
//                            dispatchReceiver = irGetAdapter(function)
//                        )
//                    )
//                )
//            }
//
//            RuiDumpPoint.KotlinLike.dump(ruiContext) {
//                println(function.dumpKotlinLike())
//            }
    }

    private fun irGetAdapter(function: IrSimpleFunction): IrExpression =
        IrGetValueImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            function.valueParameters.first().symbol
        )

    fun buildExternalPatch(ruiClass: RuiClass, parent: IrSimpleFunctionSymbol): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = irBuiltIns.unitType
        }.also { function ->

            function.parent = parent.owner
            function.visibility = DescriptorVisibilities.LOCAL

            function.addValueParameter {
                name = Name.identifier("it")
                type = ruiContext.ruiFragmentType
            }

            function.body = DeclarationIrBuilder(ruiContext.irContext, function.symbol).irBlockBody { }
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            ruiClass.builder.classBoundExternalPatchType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }
}