/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.ir.addChild
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui.RUI_IR_MISSING_FUNCTION_BODY
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.fromir.RuiFromIrTransform
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

class RuiFunctionVisitor(
    private val ruiContext: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    val ruiClasses = mutableListOf<RuiClass>()

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    fun transform(moduleFragment: IrModuleFragment) {
        ruiClasses.clear()

        moduleFragment.accept(this, null)

        ruiClasses.forEach {
            ruiContext.ruiClasses[it.irClass.kotlinFqName] = it
            it.irFunction.file.addChild(it.irClass)
        }
    }

    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (! declaration.isAnnotatedWithRui()) {
            return declaration
        }

        if (declaration.body == null) {
            RUI_IR_MISSING_FUNCTION_BODY.report(ruiContext, declaration)
            return declaration
        }

        ruiClasses += RuiFromIrTransform(ruiContext).transform(declaration)

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
    }

}
