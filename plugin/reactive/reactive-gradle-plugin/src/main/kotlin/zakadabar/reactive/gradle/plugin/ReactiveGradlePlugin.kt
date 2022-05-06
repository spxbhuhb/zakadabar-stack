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

package zakadabar.reactive.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@Suppress("unused")
class ReactiveGradlePlugin : KotlinCompilerPluginSupportPlugin {

  override fun apply(target: Project): Unit = with(target) {
    extensions.create("zakadabar.reactive", ReactiveGradleExtension::class.java)
  }

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

  override fun getCompilerPluginId(): String = BuildConfig.KOTLIN_PLUGIN_ID

  override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
    groupId = BuildConfig.KOTLIN_PLUGIN_GROUP,
    artifactId = BuildConfig.KOTLIN_PLUGIN_NAME,
    version = BuildConfig.KOTLIN_PLUGIN_VERSION
  )

  override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
    groupId = BuildConfig.KOTLIN_PLUGIN_GROUP,
    artifactId = BuildConfig.KOTLIN_PLUGIN_NAME + "-native",
    version = BuildConfig.KOTLIN_PLUGIN_VERSION
  )

  override fun applyToCompilation(
    kotlinCompilation: KotlinCompilation<*>
  ): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project

    kotlinCompilation.dependencies {
      // "hu.simplexion.zakadabar:reactive-ir-core:2022.5.4-SNAPSHOT"
      // FIXME implementation("${BuildConfig.KOTLIN_PLUGIN_GROUP}:${BuildConfig.KOTLIN_PLUGIN_NAME}:${BuildConfig.KOTLIN_PLUGIN_VERSION}")
      implementation("hu.simplexion.zakadabar:reactive-core:2022.5.4-SNAPSHOT")
    }

    val extension = project.extensions.getByType(ReactiveGradleExtension::class.java)

    return project.provider {
      listOf(
//        SubpluginOption(key = "string", value = extension.stringProperty.get()),
//        SubpluginOption(key = "file", value = extension.fileProperty.get().asFile.path),
      )
    }
  }
}
