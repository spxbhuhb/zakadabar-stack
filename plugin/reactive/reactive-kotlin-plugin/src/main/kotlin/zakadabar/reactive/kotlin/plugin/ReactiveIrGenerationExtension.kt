/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext.Companion.DUMP_BEFORE

internal class ReactiveIrGenerationExtension(
    private val reactiveContext: ReactivePluginContext
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        reactiveContext.dump(DUMP_BEFORE, moduleFragment)

//        reactiveContext.irPluginContext = pluginContext
//
//        moduleFragment.accept(TopLevelVisitor(reactiveContext), reactiveContext.controlData)
//
//        reactiveContext.dump(DUMP_RCD, reactiveContext.controlData)
//
//        moduleFragment.transform(ReactiveIrTransformer(pluginContext, reactiveContext), null)
//
//        moduleFragment.accept(ReactiveIrDebugVisitor(pluginContext, reactiveContext), null)
//
//        reactiveContext.dump(DUMP_AFTER, moduleFragment)
    }

}

