/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addDispatchReceiver
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.lazy.IrLazyFunction
import org.jetbrains.kotlin.ir.declarations.lazy.IrLazyProperty
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_RUI_CLASS
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_RUI_FUNCTION
import zakadabar.rui.kotlin.plugin.model.RuiCall
import zakadabar.rui.kotlin.plugin.model.RuiExpression
import zakadabar.rui.kotlin.plugin.util.RuiCompilationException

class RuiCallBuilder(
    override val ruiClassBuilder: RuiClassBuilder,
    val ruiCall: RuiCall
) : RuiBuilder {

    val fragmentProperty = RuiPropertyBuilder(ruiClassBuilder, Name.identifier(ruiCall.name), ruiClassBuilder.ruiFragmentType, false)

    val refClass = ruiContext.ruiClasses[ruiCall.targetRuiClass]?.irClass
        ?: irContext.referenceClass(ruiCall.targetRuiClass)?.owner

    override fun build() {
        buildFragment()
        buildPatch()
    }

    fun buildFragment() {

        if (refClass == null) {
            RUI_IR_MISSING_RUI_CLASS.report(ruiClassBuilder.ruiClass, ruiCall.irCall)
            return
        }

        irFactory.createExpressionBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET) {
            this.expression = IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                refClass.defaultType,
                refClass.primaryConstructor !!.symbol,
                0, 0,
                ruiCall.valueArguments.size + 2 // +2 = adapter + anchor
            ).also { constructorCall ->
                constructorCall.putValueArgument(0, ruiClassBuilder.adapterPropertyBuilder.irGetValue())
                constructorCall.putValueArgument(1, ruiClassBuilder.anchorPropertyBuilder.irGetValue())
                ruiCall.valueArguments.forEachIndexed { index, ruiExpression ->
                    constructorCall.putValueArgument(index + 2, ruiExpression.irExpression)
                }
            }
        }.also {
            fragmentProperty.irField.initializer = it
        }
    }

    fun buildPatch() {
        irFactory.buildFun {
            name = Name.identifier("${ruiCall.name}\$patch")
            returnType = irBuiltIns.unitType
        }.also { function ->

            function.parent = irClass

            function.addDispatchReceiver {
                type = irClass.defaultType
            }

            function.body = try {

                DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
                    ruiCall.valueArguments.mapNotNull { buildPatch(it) }.forEach { + it }
                    irCallPatch()
                }

            } catch (_: RuiCompilationException) {
                DeclarationIrBuilder(irContext, function.symbol).irBlockBody { }
            }

            ruiClassBuilder.irClass.declarations += function
        }

// FUN name:fragment0patch visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Adhoc) returnType:kotlin.Unit
//        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Adhoc
//        BLOCK_BODY
//          WHEN type=kotlin.Unit origin=IF
//            BRANCH
//              if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCLEQ
//                $this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EXCLEQ
//                  arg0: CALL 'public final fun and (other: kotlin.Int): kotlin.Int [infix] declared in kotlin.Int' type=kotlin.Int origin=null
//                    $this: CALL 'public final fun <get-dirty0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc' type=kotlin.Int origin=GET_PROPERTY
//                      $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Adhoc declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc.fragment0patch' type=zakadabar.rui.kotlin.plugin.adhoc.Adhoc origin=null
//                    other: CONST Int type=kotlin.Int value=1
//                  arg1: CONST Int type=kotlin.Int value=0
//              then: BLOCK type=kotlin.Unit origin=null
//                CALL 'public final fun <set-value> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2' type=kotlin.Unit origin=EQ
//                  $this: CALL 'public final fun <get-fragment0> (): zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2 declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2 origin=GET_PROPERTY
//                    $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Adhoc declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc.fragment0patch' type=zakadabar.rui.kotlin.plugin.adhoc.Adhoc origin=null
//                  <set-?>: CONST Int type=kotlin.Int value=12
//                CALL 'public final fun invalidate0 (index: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2' type=kotlin.Unit origin=null
//                  $this: CALL 'public final fun <get-fragment0> (): zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2 declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2 origin=GET_PROPERTY
//                    $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Adhoc declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc.fragment0patch' type=zakadabar.rui.kotlin.plugin.adhoc.Adhoc origin=null
//                  index: CONST Int type=kotlin.Int value=1
//          CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
//            $this: CALL 'public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
//              $this: CALL 'public final fun <get-fragment0> (): zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2 declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment2 origin=GET_PROPERTY
//                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Adhoc declared in zakadabar.rui.kotlin.plugin.adhoc.Adhoc.fragment0patch' type=zakadabar.rui.kotlin.plugin.adhoc.Adhoc origin=null
//
    }

    fun buildPatch(ruiExpression: RuiExpression): IrExpression? {
        // constants, globals, etc. have no dependencies, no need to patch them
        if (ruiExpression.dependencies.isEmpty()) return null


        irIf(
            buildCondition(ruiExpression),
            buildPatchResult(ruiExpression)
        )

    }

    fun buildCondition(ruiExpression: RuiExpression): IrExpression {
        val dependencies = ruiExpression.dependencies
        var result = dependencies[0].builder.irIsDirty()
        for (i in 1 until dependencies.size) {
            result = irOrOr(result, dependencies[i].builder.irIsDirty())
        }
        return result
    }

    private fun IrBlockBodyBuilder.buildPatchResult(ruiExpression: RuiExpression): IrExpression {

        checkNotNull(refClass)

        refClass.declarations.firstOrNull {
            it is IrLazyFunction && it.name.identifier == "invalidate0"
        } ?: throw RuiCompilationException(RUI_IR_MISSING_RUI_FUNCTION, ruiClassBuilder.ruiClass, ruiCall.irCall)


        return irBlock {
            //       + buildSetValueCall(ruiExpression)


            + irCall(
                refClass !!.getSimpleFunction("invalidate0") !!,
                dispatchReceiver = fragmentProperty.irGetValue()
            )
        }
    }

    fun irCallPatch() {
        checkNotNull(refClass)

        val patch = refClass.declarations.firstOrNull {
            it is IrLazyProperty && it.name.identifier == "patch"
        } ?: throw RuiCompilationException(RUI_IR_MISSING_RUI_FUNCTION, ruiClassBuilder.ruiClass, ruiCall.irCall)

        irCall(
            patch.symbol as IrFunctionSymbol,
            dispatchReceiver = fragmentProperty.irGetValue()
        )
    }

}