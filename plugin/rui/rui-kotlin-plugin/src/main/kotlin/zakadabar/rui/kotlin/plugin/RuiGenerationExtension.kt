/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_AFTER
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_BEFORE
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_RUI_TREE
import zakadabar.rui.kotlin.plugin.transform.fromir.RuiFunctionVisitor
import zakadabar.rui.kotlin.plugin.transform.toir.RuiToIrTransform

internal class RuiGenerationExtension(
    val annotations: List<String>,
    val dumpPoints: List<String>
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        val ruiContext = RuiPluginContext(
            pluginContext,
            annotations,
            dumpPoints,
            pluginContext.createDiagnosticReporter(RuiCommandLineProcessor.PLUGIN_ID),
            withTrace = true
        )

        if (DUMP_BEFORE in ruiContext.dumpPoints) {
            println("DUMP BEFORE")
            println(moduleFragment.dump())
        }

        RuiFunctionVisitor(ruiContext).also {
            moduleFragment.accept(it, null)
            RuiToIrTransform(ruiContext, it.ruiClasses).transform()
        }

        if (DUMP_RUI_TREE in ruiContext.dumpPoints) {
            println("RUI CLASSES")
            ruiContext.ruiClasses.values.forEach {
                println(it.dump())
            }
        }

        if (DUMP_AFTER in ruiContext.dumpPoints) {
            println("DUMP AFTER")
            println(moduleFragment.dump())
        }
    }

}

