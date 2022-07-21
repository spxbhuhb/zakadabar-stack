/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.ir.addFakeOverrides
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.UNDEFINED_PARAMETER_INDEX
import org.jetbrains.kotlin.ir.builders.declarations.addConstructor
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.SpecialNames
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.state.definition.RuiStateVariableVisitor
import zakadabar.rui.kotlin.plugin.state.definition.toRuiClassName

class RuiClassBuilder(
    override val ruiContext: RuiPluginContext,
    val ruiClass : RuiClass,
) : RuiBuilder {

    val irFunction = ruiClass.irFunction

    val name: String
    val fqName: FqName

    val constructor: IrConstructor
    val initializer: IrAnonymousInitializer
    val thisReceiver: IrValueParameter

    val ruiComponentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiBlock"))) !!
    val ruiComponentType = ruiComponentClass.typeWith()

    val irClass: IrClass = irFactory.buildClass {
        startOffset = irFunction.startOffset
        endOffset = irFunction.endOffset
        origin = IrDeclarationOrigin.DEFINED
        name = irFunction.name.toRuiClassName()
        kind = ClassKind.CLASS
        visibility = irFunction.visibility
        modality = Modality.OPEN
    }

    init {

        irClass.parent = irFunction.file
        irClass.superTypes = listOf(ruiComponentType)
        irClass.metadata = irFunction.metadata

        name = irClass.name.identifier
        fqName = irClass.kotlinFqName

        thisReceiver = buildThisReceiver()
        constructor = buildConstructor()
        initializer = buildInitializer()

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        RuiStateVariableVisitor(ruiContext, this).buildStateDefinition()
    }

    private fun buildThisReceiver(): IrValueParameter {

        val thisReceiver = irFactory.createValueParameter(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            IrDeclarationOrigin.INSTANCE_RECEIVER,
            IrValueParameterSymbolImpl(),
            SpecialNames.THIS,
            UNDEFINED_PARAMETER_INDEX,
            IrSimpleTypeImpl(irClass.symbol, false, emptyList(), emptyList()),
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false
        ).also {
            it.parent = irClass
            irClass.thisReceiver = it
        }

        return thisReceiver
    }

    private fun buildConstructor(): IrConstructor {

        val constructor = irClass.addConstructor {
            isPrimary = true
            returnType = irClass.typeWith()
        }

        constructor.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                ruiComponentType,
                ruiComponentClass.constructors.first()
            )

            statements += IrInstanceInitializerCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                irClass.symbol,
                unitType
            )
        }

        return constructor
    }

    private fun buildInitializer(): IrAnonymousInitializer {

        val initializer = irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        )

        initializer.parent = irClass
        initializer.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return initializer
    }

    fun build() {
        // The initializer has to be the last, so it will be able to access all properties
        irClass.declarations += initializer
    }

}