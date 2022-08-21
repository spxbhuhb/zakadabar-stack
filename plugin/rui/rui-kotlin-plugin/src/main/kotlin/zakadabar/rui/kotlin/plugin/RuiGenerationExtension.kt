/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump
import zakadabar.rui.kotlin.plugin.transform.fromir.RuiFunctionVisitor
import zakadabar.rui.kotlin.plugin.transform.toir.RuiToIrTransform
import zakadabar.rui.runtime.Plugin.PLUGIN_ID

internal class RuiGenerationExtension(
    val options: RuiOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val ruiContext = RuiPluginContext(
            pluginContext,
            options,
            pluginContext.createDiagnosticReporter(PLUGIN_ID),
        )

        RuiDumpPoint.Before.dump(ruiContext) {
            println("DUMP BEFORE")
            println(moduleFragment.dump())
        }

        RuiFunctionVisitor(ruiContext).also {
            moduleFragment.accept(it, null)
            RuiToIrTransform(ruiContext, it.ruiClasses, it.ruiEntryPoints).transform()
        }

        RuiDumpPoint.RuiTree.dump(ruiContext) {
            println("RUI CLASSES")
            ruiContext.ruiClasses.values.forEach {
                println(it.dump())
            }
        }

        RuiDumpPoint.After.dump(ruiContext) {
            println("DUMP AFTER")
            println(moduleFragment.dump())
        }
    }

}

