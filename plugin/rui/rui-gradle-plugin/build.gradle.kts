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
  packageName("zakadabar.rui.gradle.plugin")
  buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"zakadabar-rui\"")
  buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${project.group}\"")
  buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"rui-kotlin-plugin\"")
  buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${project.version}\"")
}

gradlePlugin {
  plugins {
    create("zakadabarRuiPlugin") {
      id = "zakadabar-rui"
      displayName = "Zakadabar RUI Plugin"
      description = "Zakadabar RUI Plugin"
      implementationClass = "zakadabar.rui.gradle.plugin.RuiGradlePlugin"
    }
  }
}
