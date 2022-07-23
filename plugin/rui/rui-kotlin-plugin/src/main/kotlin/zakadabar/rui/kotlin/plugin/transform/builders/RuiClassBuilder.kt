/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import org.jetbrains.kotlin.backend.common.ir.addFakeOverrides
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.UNDEFINED_PARAMETER_INDEX
import org.jetbrains.kotlin.ir.builders.declarations.addConstructor
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
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
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.util.toRuiClassName

class RuiClassBuilder(
    val ruiClass: RuiClass
) : RuiBuilder {

    override val ruiClassBuilder = this

    override val ruiContext
        get() = this.ruiClass.ruiContext

    val irFunction = ruiClass.irFunction

    val name: String
    val fqName: FqName

    val constructor: IrConstructor
    val initializer: IrAnonymousInitializer
    val thisReceiver: IrValueParameter

    lateinit var adapterPropertyBuilder: RuiPropertyBuilder
    lateinit var anchorPropertyBuilder: RuiPropertyBuilder

    val ruiFragmentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiFragment"))) !!
    val ruiFragmentType = ruiFragmentClass.typeWith()

    val ruiAdapterClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiAdapter"))) !!
    val ruiAdapterType = ruiAdapterClass.typeWith()

    override val irClass: IrClass = irFactory.buildClass {
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
        irClass.superTypes = listOf(ruiFragmentType)
        irClass.metadata = irFunction.metadata

        name = irClass.name.identifier
        fqName = irClass.kotlinFqName

        thisReceiver = initThisReceiver()
        constructor = initConstructor()
        initializer = initInitializer()
    }

    private fun initThisReceiver(): IrValueParameter {

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

    /**
     * Creates a primary constructor with a standard body (super class constructor call
     * and initializer call).
     *
     * Adds value parameters to the constructor:
     *
     * - `adapter` with type `RuiAdapter`
     * - `anchor` with type `RuiFragment`
     *
     * Later `RuiStateTransformer` adds parameters from the original function.
     */
    private fun initConstructor(): IrConstructor {

        val constructor = irClass.addConstructor {
            isPrimary = true
            returnType = irClass.typeWith()
        }

        constructor.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                ruiFragmentType,
                ruiFragmentClass.constructors.first()
            )

            statements += IrInstanceInitializerCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                irClass.symbol,
                irBuiltIns.unitType
            )
        }

        constructor.addValueParameter {
            name = Name.identifier("ruiAdapter")
            type = ruiAdapterType
        }.also {
            adapterPropertyBuilder = RuiPropertyBuilder(this, it.name, it.type, isVar = false)
        }

        constructor.addValueParameter {
            name = Name.identifier("ruiAnchor")
            type = ruiFragmentType
        }.also {
            anchorPropertyBuilder = RuiPropertyBuilder(this, it.name, it.type, isVar = false)
        }

        return constructor
    }

    private fun initInitializer(): IrAnonymousInitializer {

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

    override fun build() {

        ruiClass.rootBlock.statements.forEach {
            it.builder.build()
        }

        // The initializer has to be the last, so it will be able to access all properties
        irClass.declarations += initializer
        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))
    }

}