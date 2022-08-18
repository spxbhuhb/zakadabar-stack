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
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_AFTER
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_BEFORE
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_KOTLIN_LIKE
import zakadabar.rui.kotlin.plugin.RuiPluginContext.Companion.DUMP_RUI_TREE

/**
 * Registers the extensions into the compiler.
 */
@AutoService(ComponentRegistrar::class)
class RuiComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {


        val annotations = configuration.get(ANNOTATION).orEmpty().toMutableList()
        if (annotations.isEmpty()) annotations += "zakadabar.rui.runtime.Rui"

//        val dump = configuration.get(DUMP).orEmpty()
//        val dump = listOf(DUMP_BEFORE, DUMP_AFTER, DUMP_RUI_TREE)
        val dump = listOf(DUMP_BEFORE, DUMP_AFTER, DUMP_RUI_TREE, DUMP_KOTLIN_LIKE)

        registerRuiComponents(project, annotations, dump, configuration.getBoolean(JVMConfigurationKeys.IR))
    }

    fun registerRuiComponents(project: Project, annotations: List<String>, dump: List<String>, useIr: Boolean) {

        StorageComponentContainerContributor.registerExtension(
            project,
            RuiComponentContainerContributor(annotations, useIr)
        )

        IrGenerationExtension.registerExtension(
            project,
            RuiGenerationExtension(annotations, dump)
        )

    }

}