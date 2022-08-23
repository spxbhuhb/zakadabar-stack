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

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class RuiGradleExtension(objects: ObjectFactory) {
    val annotations: ListProperty<String> = objects.listProperty(String::class.java)
    val dumpPoints: ListProperty<String> = objects.listProperty(String::class.java)
    val rootNameStrategy: ListProperty<String> = objects.listProperty(String::class.java)
    val trace: Property<Boolean> = objects.property(Boolean::class.java).also { it.set(false) }
    val exportState: Property<Boolean> = objects.property(Boolean::class.java).also { it.set(false) }
    val importState: Property<Boolean> = objects.property(Boolean::class.java).also { it.set(false) }
}

fun org.gradle.api.Project.rui(configure: Action<RuiGradleExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("rui", configure)
