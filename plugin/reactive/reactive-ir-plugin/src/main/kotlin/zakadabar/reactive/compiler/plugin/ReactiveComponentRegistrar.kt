/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.compiler.plugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import zakadabar.reactive.compiler.plugin.ReactiveConfigurationKeys.ANNOTATION

/**
 * Registers the extensions into the compiler.
 */
@AutoService(ComponentRegistrar::class)
class ReactiveComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {

        val annotations = configuration.get(ANNOTATION).orEmpty().toMutableList()
        if (annotations.isEmpty()) annotations += "Reactive"

        registerReactiveComponents(project, annotations, configuration.getBoolean(JVMConfigurationKeys.IR))
    }

    fun registerReactiveComponents(project: Project, annotations: List<String>, useIr: Boolean) {

        StorageComponentContainerContributor.registerExtension(
            project,
            ReactiveComponentContainerContributor(annotations, useIr)
        )
        
        IrGenerationExtension.registerExtension(
            project,
            ReactiveIrGenerationExtension(annotations)
        )

    }

}
