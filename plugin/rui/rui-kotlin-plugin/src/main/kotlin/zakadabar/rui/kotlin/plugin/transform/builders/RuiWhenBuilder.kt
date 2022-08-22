/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irBranch
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.*
import zakadabar.rui.kotlin.plugin.model.RuiBranch
import zakadabar.rui.kotlin.plugin.model.RuiWhen
import zakadabar.rui.kotlin.plugin.transform.RuiClassSymbols
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException

class RuiWhenBuilder(
    override val ruiClassBuilder: RuiClassBuilder,
    val ruiWhen: RuiWhen
) : RuiFragmentBuilder {

    // we have to initialize this in build, after all other classes in the module are registered
    override lateinit var symbolMap: RuiClassSymbols

    override fun irNewInstance(): IrExpression =
        tryBuild(ruiWhen.irWhen) {
            symbolMap = ruiContext.ruiSymbolMap.getSymbolMap(RUI_FQN_WHEN_CLASS)

            IrConstructorCallImpl(
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
        }

    fun buildSelect(): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            modality = Modality.FINAL
            returnType = irBuiltIns.intType
        }.also { function ->

            function.parent = irClass
            function.visibility = DescriptorVisibilities.LOCAL

            function.body = buildSelectBody(function)
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.functionN(0).typeWith(irBuiltIns.intType),
            function,
            IrStatementOrigin.LAMBDA
        )
    }

    private fun buildSelectBody(function: IrSimpleFunction): IrBody {
        return try {

            DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
                + irReturn(
                    if (ruiWhen.irSubject == null) {
                        buildSelectWhen()
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

    fun IrBlockBodyBuilder.buildSelectWhen(): IrExpression {
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

        return IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            IrStatementOrigin.WHEN
        ).also {
            it.branches.addAll(branches)
        }
    }

    fun buildFragmentVarArg(): IrExpression {
        return IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(ruiContext.ruiFragmentFactoryType),
            ruiContext.ruiFragmentFactoryType,
        ).also { vararg ->
            ruiWhen.branches.forEach {
                vararg.addElement(buildBranchFactory(it))
            }
        }
    }

    fun buildBranchFactory(branch: RuiBranch): IrExpression =
        IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            ruiContext.ruiFragmentFactoryType,
            ruiContext.ruiFragmentFactoryClass.constructors.first(),
            0, 0, 1
        ).also { call ->
            call.putValueArgument(0, buildFactoryLambda(branch))
        }

    fun buildFactoryLambda(branch: RuiBranch): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = ruiContext.ruiFragmentType
        }.also { function ->
            function.parent = irClass
            function.visibility = DescriptorVisibilities.LOCAL
            function.body = DeclarationIrBuilder(ruiContext.irContext, function.symbol).irBlockBody {
                + irReturn(branch.result.builder.irNewInstance())
            }
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.functionN(0).typeWith(ruiContext.ruiFragmentType),
            function,
            IrStatementOrigin.LAMBDA
        )
    }
}