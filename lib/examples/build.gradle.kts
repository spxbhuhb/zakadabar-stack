/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
}

group = "hu.simplexion.zakadabar"
version = rootProject.extra["stackVersion"] as String

// common
val ktorVersion = rootProject.extra["ktorVersion"] as String

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
        nodejs()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(project(":lib:markdown"))
                implementation(project(":lib:accounts"))
                implementation(project(":lib:blobs"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
            }
        }
    }

    sourceSets["jvmTest"].dependencies {
        dependencies {
            implementation("io.ktor:ktor-server-netty:$ktorVersion")
            implementation("com.h2database:h2:1.4.200")
        }
    }
}