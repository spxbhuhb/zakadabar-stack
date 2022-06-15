/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import zakadabar.reactive.kotlin.plugin.ReactiveConfigurationKeys.ANNOTATION
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext.Companion.DUMP_BEFORE
import zakadabar.reactive.kotlin.plugin.ReactivePluginContext.Companion.DUMP_RCD

/**
 * Registers the extensions into the compiler.
 */
@AutoService(ComponentRegistrar::class)
class ReactiveComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {


        val annotations = configuration.get(ANNOTATION).orEmpty().toMutableList()
        if (annotations.isEmpty()) annotations += "zakadabar.reactive.core.Reactive"

//        val dump = configuration.get(DUMP).orEmpty()
        val dump = listOf(DUMP_BEFORE, DUMP_RCD)

        registerReactiveComponents(project, ReactivePluginContext(annotations, dump), configuration.getBoolean(JVMConfigurationKeys.IR))
    }

    fun registerReactiveComponents(project: Project, configuration: ReactivePluginContext, useIr: Boolean) {

        StorageComponentContainerContributor.registerExtension(
            project,
            ReactiveComponentContainerContributor(configuration.annotations, useIr)
        )
        
        IrGenerationExtension.registerExtension(
            project,
            ReactiveIrGenerationExtension(configuration)
        )

    }

}
