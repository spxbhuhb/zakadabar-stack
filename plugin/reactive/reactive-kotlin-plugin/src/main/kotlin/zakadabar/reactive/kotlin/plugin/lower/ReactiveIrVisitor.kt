/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.extensions.AnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner

class ReactiveIrVisitor(
    private val context: IrPluginContext,
    private val annotations: List<String>
) : AnnotationBasedExtension, IrElementVisitorVoid {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        annotations

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    override fun visitFunction(declaration: IrFunction) {
        declaration.dump()
        super.visitFunction(declaration)
        if (! isReactive(declaration)) return

        println("===============   reactive function: ${declaration.name.identifier}")
        println(declaration.dump())
    }

    override fun visitCall(expression: IrCall) {
        super.visitCall(expression)
        if (! isReactive(expression.symbol)) return

        println("===============   reactive call: ${expression.symbol.owner.name}")
        println(expression.dump())
    }

    override fun visitClass(declaration: IrClass) {
        super.visitClass(declaration)
        if (! isReactive(declaration)) return
    }

    private fun isReactive(declaration: IrClass): Boolean =
        declaration.kind == ClassKind.CLASS &&
            declaration.isAnnotatedWithReactive()

    private fun isReactive(declaration: IrFunction): Boolean =
        declaration.isAnnotatedWithReactive()

    private fun isReactive(declaration: IrSimpleFunctionSymbol): Boolean =
        declaration.owner.isAnnotatedWithReactive()

    private fun IrClass.isAnnotatedWithReactive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    private fun IrFunction.isAnnotatedWithReactive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

}
