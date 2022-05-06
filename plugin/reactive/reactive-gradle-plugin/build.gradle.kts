/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.gradle.Versions

plugins {
  id("java-gradle-plugin")
  kotlin("jvm")
  id("com.github.gmazzo.buildconfig")
}

dependencies {
  implementation(kotlin("gradle-plugin-api"))
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

buildConfig {
  packageName("zakadabar.reactive.gradle.plugin")
  buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"zakadabar-reactive\"")
  buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${project.group}\"")
  buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"reactive-kotlin-plugin\"")
  buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${project.version}\"")
}

gradlePlugin {
  plugins {
    create("zakadabarReactivePlugin") {
      id = "zakadabar-reactive"
      displayName = "Zakadabar Reactive Plugin"
      description = "Zakadabar Reactive Plugin"
      implementationClass = "zakadabar.reactive.gradle.plugin.ReactiveGradlePlugin"
    }
  }
}
