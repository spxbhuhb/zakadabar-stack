/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions
import zakadabar.gradle.config
import zakadabar.gradle.isPublishing

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

    js(IR) {
        nodejs()
    }

    sourceSets["commonMain"].dependencies {
        implementation(project(":core:core"))
        implementation(project(":lib:accounts"))
        implementation(project(":lib:blobs"))
        implementation(project(":lib:schedule"))
    }

    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(kotlin("test-junit"))
    }

    sourceSets["jvmMain"].dependencies {
        implementation("org.apache.lucene:lucene-core:9.1.0")
        implementation("org.apache.lucene:lucene-queryparser:9.1.0")
    }

    sourceSets["jvmTest"].dependencies {
        implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
        implementation("com.h2database:h2:${Versions.h2}")
    }
}

if (project.isPublishing) {

    zakadabar.gradle.manifestAndDokka(tasks)

    signing { config(publishing.publications) }

    publishing {
        config(project)

        publications.withType<MavenPublication>().all {
            config(tasks["javadocJar"], "Zakadabar Lib Lucene")
        }
    }

}