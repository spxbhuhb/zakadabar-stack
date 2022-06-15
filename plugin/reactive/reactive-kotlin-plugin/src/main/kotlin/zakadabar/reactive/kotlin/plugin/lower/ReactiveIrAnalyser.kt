/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.lower

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext

class ReactiveIrAnalyser(
    private val context: ReactivePluginContext,
) : ReactiveAnnotationBasedExtension, IrElementVisitor<Unit, ReactiveControlData> {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        context.annotations

    override fun visitElement(element: IrElement, data : ReactiveControlData) {
        element.acceptChildren(this, data)
    }

    override fun visitFunction(declaration: IrFunction, data : ReactiveControlData) {
        super.visitFunction(declaration, data)
        if (! isReactive(declaration)) return

        RcdFunction(declaration.symbol).also {
            data.functions[declaration.symbol] = it
            ReactiveIrFunctionVisitor(context).visitFunction(declaration, it)
        }
    }

    override fun visitClass(declaration: IrClass, data : ReactiveControlData) {
        super.visitClass(declaration, data)
        if (! isReactive(declaration)) return
    }

}
