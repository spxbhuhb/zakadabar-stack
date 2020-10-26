/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.4.0" apply false
    kotlin("plugin.serialization") version "1.4.0" apply false
    id("org.jetbrains.dokka") version "1.4.0-rc" apply false
    id("com.github.johnrengelman.shadow") version "6.0.0" apply false
    signing
    `maven-publish`
}

version = "2020.10.26-SNAPSHOT"

subprojects {

    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx/") // for kotlinx.datetime, to be changed to jcenter()
    }

    // The core has its own build.gradle.kts, examples is just a placeholder.
    if (name in listOf("samples", "core")) return@subprojects

    // Common configuration for the examples

    group = "hu.simplexion.zakadabar.samples"

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "1.8"
    }

    apply(plugin = "kotlin-multiplatform")

    configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {

        jvm()

        js {
            browser()
        }

        sourceSets["commonMain"].dependencies {
            implementation(project(":core"))
        }
    }

}
