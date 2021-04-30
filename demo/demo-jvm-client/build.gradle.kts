/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    application
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = "2021.4.30"

application {
    mainClassName = "zakadabar.demo.jvm.frontend.MainKt"
}

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

    sourceSets["commonMain"].dependencies {
        implementation(project(":core"))
        implementation(project(":demo:demo-marina"))
    }
}
