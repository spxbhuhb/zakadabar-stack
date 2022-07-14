/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

class RuiCommandLineProcessor : CommandLineProcessor {
    companion object {
        val OPTION_ANNOTATION = CliOption(
            "annotation", "<fqname>", "Annotation qualified names",
            required = false, allowMultipleOccurrences = true
        )
        val OPTION_DUMP = CliOption(
            "dump", "string", "Dump data at specified points of plugin run(before, after, rcd).",
            required = false, allowMultipleOccurrences = true
        )
        const val PLUGIN_ID = "zakadabar.rui"
    }

    override val pluginId = zakadabar.rui.kotlin.plugin.RuiCommandLineProcessor.Companion.PLUGIN_ID
    override val pluginOptions = listOf(
        zakadabar.rui.kotlin.plugin.RuiCommandLineProcessor.Companion.OPTION_ANNOTATION,
        zakadabar.rui.kotlin.plugin.RuiCommandLineProcessor.Companion.OPTION_DUMP
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        return when (option) {
            zakadabar.rui.kotlin.plugin.RuiCommandLineProcessor.Companion.OPTION_ANNOTATION -> configuration.appendList(zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.ANNOTATION, value)
            zakadabar.rui.kotlin.plugin.RuiCommandLineProcessor.Companion.OPTION_DUMP -> configuration.appendList(zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.DUMP, value)
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
    }
}
