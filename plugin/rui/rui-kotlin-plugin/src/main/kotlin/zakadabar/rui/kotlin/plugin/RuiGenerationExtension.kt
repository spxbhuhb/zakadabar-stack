/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_AFTER
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_BEFORE
import zakadabar.rui.kotlin.plugin.lower.RuiCompilationPhase
import zakadabar.rui.kotlin.plugin.lower.RuiFunctionVisitor

internal class RuiGenerationExtension(
    private val ruiPluginContext: RuiPluginContext
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        ruiPluginContext.irPluginContext = pluginContext
        ruiPluginContext.diagnosticReporter = pluginContext.createDiagnosticReporter(RuiCommandLineProcessor.PLUGIN_ID)

        ruiPluginContext.dump(DUMP_BEFORE, moduleFragment)

        moduleFragment.accept(RuiFunctionVisitor(ruiPluginContext, RuiCompilationPhase.StateDefinition), null)

        moduleFragment.accept(RuiFunctionVisitor(ruiPluginContext, RuiCompilationPhase.Rendering), null)

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
        ruiPluginContext.dump(DUMP_AFTER, moduleFragment)
    }

}

