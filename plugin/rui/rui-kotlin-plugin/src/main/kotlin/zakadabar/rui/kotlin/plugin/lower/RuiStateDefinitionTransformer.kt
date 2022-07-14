/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrSetFieldImpl
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassBuilder

/**
 * State of a component contains:
 *
 * - parameters from the original function, each parameter generates a property
 *   in the class
 * - top level variables declared in the function, each variable generates
 *   a property in the class
 *
 * Properties generated from original function parameters are passed as
 * constructor parameters.
 *
 * Properties generated from top level variables are initialized by the
 * constructor. The state definition part of the original function is
 * transformed into a constructor.
 */
class RuiStateDefinitionTransformer(
    private val ruiContext: RuiPluginContext,
    private val builder: RuiClassBuilder
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    val irContext = ruiContext.irPluginContext
    val irFactory = irContext.irFactory

    val unitType = irContext.irBuiltIns.unitType

    fun buildStateDefinition() {

        val constructor = builder.addConstructor()
        val initializer = builder.addInitializer()

        // parameters of the function into constructor parameters and properties

        builder.original.valueParameters.forEach { original ->
            constructor.addValueParameter {
                name = original.name
                type = original.type
                varargElementType = original.varargElementType
            }.also { new ->
                builder.addProperty(new)
            }
        }

        // statements of the function into properties and statements in constructor

        builder.original.body?.statements?.let { statements ->
            statements
                .subList(0, builder.boundary)
                .forEach { irStatement ->
                    when (irStatement) {
                        is IrVariable -> addVariable(initializer, irStatement)
                        // the deep copy below is necessary, otherwise the original function code is transformed
                        // TODO maybe remove deep copy when the plugin replaces the body of the original function, I'm not sure about this
                        else -> initializer.body.statements += irStatement.deepCopyWithSymbols().transformStatement(this)
                    }
                }
        }
    }

    private fun addVariable(initializer: IrAnonymousInitializer, irVariable: IrVariable) {

        // Add a class property and initializer for this variable. We have to transform the
        // initializer code as it may contain references to other variables processed before.

        builder.addProperty(irVariable).also { property ->

            irVariable.initializer?.let {

                val receiver = checkNotNull(builder.irClass.thisReceiver)
                val field = checkNotNull(property.backingField)

                initializer.body.statements += IrSetFieldImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    symbol = field.symbol,
                    type = field.type,
                    receiver = IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, receiver.symbol),
                    // the deep copy below is necessary, otherwise the original function code is transformed
                    // TODO maybe remove deep copy when the plugin replaces the body of the original function, I'm not sure about this
                    value = it.deepCopyWithSymbols().transform(this, null),
                    origin = IrStatementOrigin.INITIALIZE_FIELD
                )
            }
        }
    }

    override fun visitGetValue(expression: IrGetValue): IrExpression {

        val owner = expression.symbol.owner
        val parent = owner.parent

        if (parent !is IrFunction || parent.symbol != builder.original.symbol) {
            return super.visitGetValue(expression)
        }

        val property = builder.properties[owner.name.identifier] ?: throw IllegalStateException("missing property ${owner.name.identifier} in ${parent.name}")

        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            expression.type,
            property.getter !!.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 0,
        ).apply {

            dispatchReceiver = IrGetValueImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                builder.irClass.thisReceiver !!.symbol
            )

            return this
        }
    }

}
