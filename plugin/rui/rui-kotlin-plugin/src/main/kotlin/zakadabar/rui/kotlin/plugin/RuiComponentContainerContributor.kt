/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.jvm.isJvm
import zakadabar.rui.kotlin.plugin.diagnostics.RuiDeclarationChecker

class RuiComponentContainerContributor(
    private val annotations: List<String>,
    private val useIr: Boolean,
) : StorageComponentContainerContributor {

    override fun registerModuleComponents(
        container: StorageComponentContainer, platform: TargetPlatform, moduleDescriptor: ModuleDescriptor
    ) {
        if (!platform.isJvm()) return

        container.useInstance(RuiDeclarationChecker(annotations, useIr))
    }
}
