/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.ANNOTATION
import zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.DUMP
import zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.EXPORT_STATE
import zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.IMPORT_STATE
import zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.ROOT_NAME_STRATEGY
import zakadabar.rui.kotlin.plugin.RuiConfigurationKeys.TRACE
import zakadabar.rui.runtime.Plugin.RUI_ANNOTATION

/**
 * Registers the extensions into the compiler.
 */
@AutoService(ComponentRegistrar::class)
class RuiComponentRegistrar(
    val dumpPoints: List<RuiDumpPoint> = emptyList(),
    val rootNameStrategy: RuiRootNameStrategy = RuiRootNameStrategy.StartOffset,
    val trace: Boolean = false,
    val exportState: Boolean = false,
    val importState: Boolean = false
) : ComponentRegistrar {

    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {

        val annotations = configuration.get(ANNOTATION).let { if (it != null && it.isNotEmpty()) it else listOf(RUI_ANNOTATION) }
        val dumpPoints = configuration.get(DUMP) ?: dumpPoints
        val rootNameStrategy = configuration.get(ROOT_NAME_STRATEGY) ?: rootNameStrategy
        val trace = configuration.get(TRACE) ?: trace
        val exportState = configuration.get(EXPORT_STATE) ?: exportState
        val importState = configuration.get(IMPORT_STATE) ?: importState

        val options = RuiOptions(annotations, dumpPoints, rootNameStrategy, trace, exportState, importState)

        registerRuiComponents(project, options, configuration.getBoolean(JVMConfigurationKeys.IR))
    }

    fun registerRuiComponents(project: Project, options: RuiOptions, useIr: Boolean) {

        StorageComponentContainerContributor.registerExtension(
            project,
            RuiComponentContainerContributor(options.annotations, useIr)
        )

        IrGenerationExtension.registerExtension(
            project,
            RuiGenerationExtension(options)
        )

    }

    companion object {
        fun withAll() =
            RuiComponentRegistrar(
                RuiDumpPoint.values().toList(),
                RuiRootNameStrategy.NoPostfix,
                trace = true,
                exportState = true,
                importState = true
            )
    }

}
