/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext.Companion.DUMP_AFTER
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext.Companion.DUMP_BEFORE
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext.Companion.DUMP_RCD
import zakadabar.reactive.kotlin.plugin.lower.ReactiveIrAnalyser

internal class ReactiveIrGenerationExtension(
    private val reactiveContext: ReactivePluginContext
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        reactiveContext.dump(DUMP_BEFORE, moduleFragment)

        reactiveContext.irPluginContext = pluginContext

        moduleFragment.accept(ReactiveIrAnalyser(reactiveContext), reactiveContext.controlData)

        reactiveContext.dump(DUMP_RCD, reactiveContext.controlData)

        // moduleFragment.transform(ReactiveIrTransformer(pluginContext, configuration), controlData)

        // moduleFragment.accept(ReactiveIrVisitor(pluginContext, configuration), controlData)

        reactiveContext.dump(DUMP_AFTER, moduleFragment)
    }

}

