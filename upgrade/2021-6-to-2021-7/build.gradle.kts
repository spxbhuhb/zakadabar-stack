/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    id("com.palantir.docker") version "0.25.0"
    application
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = rootProject.extra["stackVersion"] as String

application {
    getMainClass().set("zakadabar.upgrade.u2021_6_to_2021_7.MainKt")
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

    js {
        browser()
    }

    sourceSets["commonMain"].dependencies {
        implementation(project(":core"))
        implementation(project(":lib:accounts"))
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveFileName.set("2021.6-to-${project.version}.jar")
    minimize()
}

signing {
    useGpgCmd()
    sign(tasks["shadowJar"])
}
