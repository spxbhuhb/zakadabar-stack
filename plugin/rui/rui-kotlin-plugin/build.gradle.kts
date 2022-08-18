/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.gradle.Versions

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.gmazzo.buildconfig")
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    kapt("com.google.auto.service:auto-service:1.0")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.8")
    testImplementation(project(":plugin:rui:rui-runtime"))
}

buildConfig {
    packageName("zakadabar.rui.kotlin.plugin")
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"zakadabar-rui\"")
}

tasks.test {
    testLogging.showStandardStreams = true
}