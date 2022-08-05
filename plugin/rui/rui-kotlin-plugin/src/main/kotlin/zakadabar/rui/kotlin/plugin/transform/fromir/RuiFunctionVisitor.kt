/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.fromir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_FUNCTION_BODY
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.RUI_ENTRY_FUNCTION
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension
import kotlin.collections.List
import kotlin.collections.mutableListOf
import kotlin.collections.plusAssign
import kotlin.collections.set

class RuiFunctionVisitor(
    private val ruiContext: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    val ruiClasses = mutableListOf<RuiClass>()

    val irBuiltIns = ruiContext.irContext.irBuiltIns

    val irFactory = ruiContext.irContext.irFactory

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitClassNew(declaration: IrClass): IrStatement {
        return super.visitClassNew(declaration)
    }

    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (!declaration.isAnnotatedWithRui()) {
            return super.visitFunctionNew(declaration) as IrFunction
        }

        if (declaration.body == null) {
            RUI_IR_MISSING_FUNCTION_BODY.report(ruiContext, declaration)
            return super.visitFunctionNew(declaration) as IrFunction
        }

        RuiFromIrTransform(ruiContext, declaration).transform().also {
            ruiClasses += it
            ruiContext.ruiClasses[it.irClass.kotlinFqName] = it
        }

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
    }

    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol.owner.kotlinFqName != RUI_ENTRY_FUNCTION) {
            return super.visitCall(expression)
        }

        if (expression.valueArgumentsCount == 0) {
            ErrorsRui.RUI_IR_INTERNAL_PLUGIN_ERROR.report(ruiContext, expression, "${expression.symbol} value arguments count == 0")
            return super.visitCall(expression)
        }

        val block = expression.getValueArgument(expression.valueArgumentsCount - 1)

        if (block !is IrFunctionExpression) {
            ErrorsRui.RUI_IR_INTERNAL_PLUGIN_ERROR.report(ruiContext, expression, "${expression.symbol} last parameter is not a function expression")
            return super.visitCall(expression)
        }

        val ruiClass : RuiClass

        RuiFromIrTransform(ruiContext, block.function).transform().also {
            ruiClasses += it
            ruiContext.ruiClasses[it.irClass.kotlinFqName] = it
            ruiClass = it
        }

        block.function.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).also {

            val adapterFor = IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                ruiContext.ruiAdapterType,
                ruiContext.ruiAdapterRegistryClass.functionByName("adapterFor"),
                0, 1
            ).also { call ->
                call.dispatchReceiver = IrGetObjectValueImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    ruiContext.ruiAdapterRegistryType,
                    ruiContext.ruiAdapterRegistryClass
                )
                call.putValueArgument(0, buildAdapterVarArg())
            }


            it.statements += IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                ruiClass.irClass.defaultType,
                ruiClass.builder.constructor.symbol,
                0, 0, 2
            ).also { call ->
                call.putValueArgument(0, adapterFor)
                call.putValueArgument(1, buildExternalPatch(block.function.symbol))
            }

        }

        return expression
    }

    fun buildAdapterVarArg(): IrExpression {
        return IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(irBuiltIns.anyType.makeNullable()),
            ruiContext.ruiFragmentType,
        )
        // TODO copy arguments from the original function call
    }

    fun buildExternalPatch(parent: IrSimpleFunctionSymbol): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = irBuiltIns.unitType
        }.also { function ->

            function.parent = parent.owner
            function.visibility = DescriptorVisibilities.LOCAL

            val patchStateIt = function.addValueParameter {
                name = Name.identifier("it")
                type = ruiContext.ruiFragmentType
            }

            function.body = DeclarationIrBuilder(ruiContext.irContext, function.symbol).irBlockBody { }
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            ruiContext.ruiExternalPatchType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }

}
