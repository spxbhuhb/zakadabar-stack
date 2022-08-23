/*
 * Copyright (C) 2020 Brian Norman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zakadabar.rui.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import zakadabar.rui.runtime.Plugin.GRADLE_EXTENSION_NAME
import zakadabar.rui.runtime.Plugin.KOTLIN_PLUGIN_NAME
import zakadabar.rui.runtime.Plugin.OPTION_NAME_ANNOTATION
import zakadabar.rui.runtime.Plugin.OPTION_NAME_DUMP_POINT
import zakadabar.rui.runtime.Plugin.OPTION_NAME_EXPORT_STATE
import zakadabar.rui.runtime.Plugin.OPTION_NAME_IMPORT_STATE
import zakadabar.rui.runtime.Plugin.OPTION_NAME_ROOT_NAME_STRATEGY
import zakadabar.rui.runtime.Plugin.OPTION_NAME_TRACE
import zakadabar.rui.runtime.Plugin.PLUGIN_GROUP
import zakadabar.rui.runtime.Plugin.PLUGIN_ID
import zakadabar.rui.runtime.Plugin.PLUGIN_VERSION
import zakadabar.rui.runtime.Plugin.RUNTIME_NAME

@Suppress("unused")
class RuiGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project): Unit = with(target) {
        extensions.create(GRADLE_EXTENSION_NAME, RuiGradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.target.project.plugins.hasPlugin(RuiGradlePlugin::class.java)

    override fun getCompilerPluginId(): String = PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = PLUGIN_GROUP,
        artifactId = KOTLIN_PLUGIN_NAME,
        version = PLUGIN_VERSION
    )

    override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
        groupId = PLUGIN_GROUP,
        artifactId = "$KOTLIN_PLUGIN_NAME-native",
        version = PLUGIN_VERSION
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project

        kotlinCompilation.dependencies {
            implementation("$PLUGIN_GROUP:$RUNTIME_NAME:$PLUGIN_VERSION")
        }

        val extension = project.extensions.getByType(RuiGradleExtension::class.java)

        val options = mutableListOf<SubpluginOption>()

        extension.annotations.get().forEach { options += SubpluginOption(key = OPTION_NAME_ANNOTATION, value = it) }
        extension.dumpPoints.get().forEach { options += SubpluginOption(key = OPTION_NAME_DUMP_POINT, value = it) }
        options += SubpluginOption(key = OPTION_NAME_ROOT_NAME_STRATEGY, extension.rootNameStrategy.get().toString())
        options += SubpluginOption(key = OPTION_NAME_TRACE, extension.trace.get().toString())
        options += SubpluginOption(key = OPTION_NAME_EXPORT_STATE, extension.exportState.get().toString())
        options += SubpluginOption(key = OPTION_NAME_IMPORT_STATE, extension.importState.get().toString())

        return project.provider { options }
    }
}
