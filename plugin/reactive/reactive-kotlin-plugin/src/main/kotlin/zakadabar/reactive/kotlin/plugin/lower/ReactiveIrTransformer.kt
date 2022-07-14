/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.lower

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext
import zakadabar.reactive.kotlin.plugin.builder.ReactiveClassBuilder

class ReactiveIrTransformer(
    private val context: IrPluginContext,
    private val configuration: ReactivePluginContext
) : IrElementTransformerVoidWithContext(), ReactiveAnnotationBasedExtension {

    val data = configuration.controlData

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        configuration.annotations

    val classBuilder = ReactiveClassBuilder(context, configuration)

    override fun visitFileNew(declaration: IrFile): IrFile {
        data.files[declaration.symbol]?.let { rFile ->
            rFile.functions.forEach { entry ->
                declaration.declarations += classBuilder.buildReactiveClass(declaration, entry.value)
            }
        }
        return declaration
    }

}
