/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.gradle.Versions

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
    implementation(project(":plugin:rui:rui-runtime"))
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

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
