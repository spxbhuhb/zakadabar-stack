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
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
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

    private lateinit var select: IrSimpleFunction
    private var branches = mutableListOf<IrSimpleFunction>()

    override fun buildDeclarations() {
        irClass.declarations += irSelect()
        ruiWhen.branches.forEach { branch ->
            branch.result.builder.buildDeclarations()
            irClass.declarations += irBranch(branch)
        }
    }

    private fun irSelect(): IrSimpleFunction =
        irFactory.buildFun {
            name = Name.identifier("$RUI_SELECT${ruiWhen.irWhen.startOffset}")
            modality = Modality.FINAL
            returnType = irBuiltIns.intType
        }.also { function ->

            function.parent = irClass
            function.visibility = DescriptorVisibilities.LOCAL

            function.body = irSelectBody(function)

            select = function
        }

    private fun irSelectBody(function: IrSimpleFunction): IrBody {
        return try {

            DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
                + irReturn(
                    if (ruiWhen.irSubject == null) {
                        irSelectWhen()
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

    private fun IrBlockBodyBuilder.irSelectWhen(): IrExpression {
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

    private fun irBranch(branch: RuiBranch): IrSimpleFunction =
        irFactory.buildFun {
            name = Name.special("$RUI_BRANCH${branch.irBranch.startOffset}")
            modality = Modality.FINAL
            returnType = classBoundFragmentType
        }.also { function ->
            function.parent = irClass
            function.visibility = DescriptorVisibilities.LOCAL
            function.body = DeclarationIrBuilder(ruiContext.irContext, function.symbol).irBlockBody {
                + irReturn(branch.result.builder.irNewInstance())
            }
            branches += function
        }

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
                constructorCall.putValueArgument(RUI_WHEN_ARGUMENT_INDEX_SELECT, irSelectReference())
                constructorCall.putValueArgument(RUI_WHEN_ARGUMENT_INDEX_FRAGMENTS, irBuilderVarargs())

            }
        }

    fun irSelectReference(): IrExpression {
        val functionType = irBuiltIns.functionN(0).typeWith(irBuiltIns.intType)

        return IrFunctionReferenceImpl.fromSymbolOwner(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            functionType,
            select.symbol,
            typeArgumentsCount = 0,
            reflectionTarget = null
        )
    }

    fun irBuilderVarargs(): IrExpression {
        return IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(),
            classBoundFragmentType,
        ).also { vararg ->
            branches.forEach {
                vararg.addElement(irBranchReference(it))
            }
        }
    }

    private fun irBranchReference(it: IrSimpleFunction): IrVarargElement {
        val functionType = irBuiltIns.functionN(0).typeWith(classBoundFragmentType)

        return IrFunctionReferenceImpl.fromSymbolOwner(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            functionType,
            select.symbol,
            typeArgumentsCount = 0,
            reflectionTarget = null
        )
    }

}