/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

noArg {
    annotation("kotlinx.serialization.Serializable")
}

kotlin {

    jvm {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js(BOTH) {
        nodejs()
    }

    sourceSets["commonMain"].dependencies {
        implementation(project(":core:core"))
    }

    sourceSets["jsMain"].dependencies {
        implementation(npm("highlight.js", Versions.highlightJs))
        api("org.jetbrains:markdown:${Versions.markdown}")
    }
}