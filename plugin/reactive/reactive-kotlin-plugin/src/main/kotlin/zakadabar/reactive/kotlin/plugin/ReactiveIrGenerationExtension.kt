/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import zakadabar.reactive.kotlin.plugin.lower.ReactiveIrTransformer
import zakadabar.reactive.kotlin.plugin.lower.ReactiveIrVisitor

internal class ReactiveIrGenerationExtension(
    private val annotations: List<String>
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        println("================  BEFORE  ================")
        moduleFragment.accept(ReactiveIrVisitor(pluginContext, annotations), null)
        moduleFragment.transform(ReactiveIrTransformer(pluginContext, annotations), null)
        println("================  AFTER  ================")
        moduleFragment.accept(ReactiveIrVisitor(pluginContext, annotations), null)
//        println(moduleFragment.dump())
    }

}

