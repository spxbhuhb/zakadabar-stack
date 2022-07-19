/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.rendering

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.builder.RuiClass
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

class RuiDependencyVisitor(
    private val ruiClass: RuiClass
) : RuiAnnotationBasedExtension, IrElementVisitorVoid {

    val dependencies = mutableListOf<Int>()

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiClass.ruiContext.annotations

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    /**
     * State variable reads are calls to the getter.
     */
    override fun visitCall(expression: IrCall) {
        ruiClass.stateVariableByGetterOrNull(expression.symbol)?.let {
            dependencies += it.index
        }
        super.visitCall(expression)
    }


}
