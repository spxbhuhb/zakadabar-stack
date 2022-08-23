/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import zakadabar.rui.runtime.Plugin.OPTION_NAME_ANNOTATION
import zakadabar.rui.runtime.Plugin.OPTION_NAME_DUMP_POINT
import zakadabar.rui.runtime.Plugin.OPTION_NAME_EXPORT_STATE
import zakadabar.rui.runtime.Plugin.OPTION_NAME_IMPORT_STATE
import zakadabar.rui.runtime.Plugin.OPTION_NAME_ROOT_NAME_STRATEGY
import zakadabar.rui.runtime.Plugin.OPTION_NAME_TRACE
import zakadabar.rui.runtime.Plugin.PLUGIN_ID

class RuiCommandLineProcessor : CommandLineProcessor {
    companion object {

        val OPTION_ANNOTATION = CliOption(
            OPTION_NAME_ANNOTATION, "<fqname>", "Annotation qualified names",
            required = false, allowMultipleOccurrences = true
        )
        val OPTION_DUMP = CliOption(
            OPTION_NAME_DUMP_POINT, "string", "Dump data at specified points of plugin run (${RuiDumpPoint.optionValues().joinToString { ", " }}).",
            required = false, allowMultipleOccurrences = true
        )
        val OPTION_ROOT_NAME_STRATEGY = CliOption(
            OPTION_NAME_ROOT_NAME_STRATEGY, "string", "Select root name strategy (${RuiRootNameStrategy.optionValues().joinToString { ", " }}",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_TRACE = CliOption(
            OPTION_NAME_TRACE, "boolean", "Add trace output to the generated code.",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_EXPORT_STATE = CliOption(
            OPTION_NAME_EXPORT_STATE, "boolean", "Generate state export functions",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_IMPORT_STATE = CliOption(
            OPTION_NAME_IMPORT_STATE, "boolean", "Generate state import functions",
            required = false, allowMultipleOccurrences = false
        )

    }

    override val pluginId = PLUGIN_ID

    override val pluginOptions = listOf(
        OPTION_ANNOTATION,
        OPTION_DUMP,
        OPTION_ROOT_NAME_STRATEGY,
        OPTION_TRACE,
        OPTION_EXPORT_STATE,
        OPTION_IMPORT_STATE
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option) {
            OPTION_ANNOTATION -> configuration.appendList(RuiConfigurationKeys.ANNOTATION, value)
            OPTION_DUMP -> configuration.appendList(RuiConfigurationKeys.DUMP, value.toDumpPoint())
            OPTION_ROOT_NAME_STRATEGY -> configuration.put(RuiConfigurationKeys.ROOT_NAME_STRATEGY, value.toRootNameStrategy())
            OPTION_TRACE -> configuration.put(RuiConfigurationKeys.TRACE, value.toBooleanStrictOrNull() ?: false)
            OPTION_EXPORT_STATE -> configuration.put(RuiConfigurationKeys.EXPORT_STATE, value.toBooleanStrictOrNull() ?: false)
            OPTION_IMPORT_STATE -> configuration.put(RuiConfigurationKeys.IMPORT_STATE, value.toBooleanStrictOrNull() ?: false)
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
    }

    private fun String.toDumpPoint(): RuiDumpPoint =
        RuiDumpPoint.fromOption(this) ?: throw CliOptionProcessingException("Unknown dump point: $this")

    private fun String.toRootNameStrategy(): RuiRootNameStrategy =
        RuiRootNameStrategy.fromOption(this) ?: throw CliOptionProcessingException("Unknown root name strategy: $this")
}

