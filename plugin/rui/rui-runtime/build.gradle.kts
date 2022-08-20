/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions
import zakadabar.gradle.config
import zakadabar.gradle.isPublishing
import zakadabar.gradle.manifestAndDokka

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
    id("com.github.gmazzo.buildconfig")
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js(IR) {
        browser()
        nodejs()
        binaries.library()
    }

    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

buildConfig {
    packageName("zakadabar.rui.runtime")
    buildConfigField("String", "PLUGIN_VERSION", "\"${project.version}\"")
}

if (project.isPublishing) {

    manifestAndDokka(tasks)

    signing { config(publishing.publications) }

    publishing {
        config(project)

        publications.withType<MavenPublication>().all {
            config(tasks["javadocJar"], "Zakadabar Plugin RUI")
        }
    }

}