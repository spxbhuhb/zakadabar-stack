/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClassBuilder

class RuiTransformer(
    private val context: IrPluginContext,
    private val configuration: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    val data = configuration.controlData

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        configuration.annotations

    val classBuilder = RuiClassBuilder(context, configuration)

    override fun visitFileNew(declaration: IrFile): IrFile {
        data.files[declaration.symbol]?.let { rFile ->
            rFile.functions.forEach { entry ->
                declaration.declarations += classBuilder.buildRuiClass(declaration, entry.value)
            }
        }
        return declaration
    }

}
