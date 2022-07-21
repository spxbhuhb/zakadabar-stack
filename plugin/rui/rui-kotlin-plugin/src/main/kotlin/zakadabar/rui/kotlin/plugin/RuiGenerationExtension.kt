/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_AFTER
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_BEFORE
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_RUI_TREE
import zakadabar.rui.kotlin.plugin.transform.RuiFunctionVisitor

internal class RuiGenerationExtension(
    private val ruiPluginContext: RuiPluginContext
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        ruiPluginContext.irPluginContext = pluginContext
        ruiPluginContext.diagnosticReporter = pluginContext.createDiagnosticReporter(RuiCommandLineProcessor.PLUGIN_ID)

        ruiPluginContext.dump(DUMP_BEFORE, moduleFragment)

        RuiFunctionVisitor(ruiPluginContext).transform(moduleFragment)

        if (DUMP_RUI_TREE in ruiPluginContext.dumpPoints) {
            println("RUI CLASSES")
            ruiPluginContext.ruiClasses.values.filter { ! it.irClass.name.identifier.matches("RuiP\\d+".toRegex()) }.forEach {
                println(it.dump())
            }
        }

        ruiPluginContext.dump(DUMP_AFTER, moduleFragment)
    }

}

