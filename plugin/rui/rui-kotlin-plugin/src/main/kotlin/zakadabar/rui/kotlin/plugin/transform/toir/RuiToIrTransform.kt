/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.toir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.ir.addChild
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_KOTLIN_LIKE
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.util.RuiAnnotationBasedExtension

class RuiToIrTransform(
    private val ruiContext: RuiPluginContext,
    val ruiClasses : List<RuiClass>
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    fun transform() {
        ruiClasses.forEach {
            it.builder.build()
            it.irFunction.file.addChild(it.irClass)
            if (DUMP_KOTLIN_LIKE in ruiContext.dumpPoints) {
                println(it.irClass.dumpKotlinLike())
            }
        }
    }
}