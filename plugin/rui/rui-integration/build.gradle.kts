/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.6.20"
    id("zakadabar-rui") version "2022.6.2"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                kotlinOptions.jvmTarget = "1.8"
            }
        }
    }
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {

        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

// Fixes webpack-cli incompatibility by pinning the newest version.
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}

rui {
    dumpPoints.set(listOf("before", "after", "rui-tree", "kotlin-like"))
    trace.set(true)
    exportState.set(true)
    importState.set(true)
}