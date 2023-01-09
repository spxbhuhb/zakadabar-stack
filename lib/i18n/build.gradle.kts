/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions
import zakadabar.gradle.config
import zakadabar.gradle.isPublishing
import zakadabar.gradle.manifestAndDokka

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
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js(IR) {
        nodejs()
    }

    sourceSets["commonMain"].dependencies {
        implementation(project(":core:core"))
    }

}

if (project.isPublishing) {

    manifestAndDokka(tasks)

    signing { config(publishing.publications) }

    publishing {
        config(project)

        publications.withType<MavenPublication>().all {
            config(tasks["javadocJar"], "Zakadabar Lib I18N")
        }
    }

}