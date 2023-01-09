/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    id("com.palantir.docker") version "0.25.0"
    application
    signing

    id("zk-sync-build-info") apply false
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

application {
    mainClass.set("zakadabar.core.server.ServerKt")
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

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets["commonMain"].dependencies {
        api(project(":core:core"))
        api(project(":lib:accounts"))
        api(project(":lib:i18n"))
        api(project(":lib:blobs"))
        api(project(":cookbook"))
        implementation("com.h2database:h2:${Versions.h2}")
//        implementation(compose.runtime)
    }

//    sourceSets["jsMain"].dependencies {
//        implementation(compose.web.core)
//    }
}

tasks.withType<Jar> {
    manifest {
        attributes += sortedMapOf(
            "Built-By" to System.getProperty("user.name"),
            "Build-Jdk" to System.getProperty("java.version"),
            "Implementation-Vendor" to "Simplexion Kft.",
            "Implementation-Version" to archiveVersion.get(),
            "Created-By" to org.gradle.util.GradleVersion.current()
        )
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    // seems like this does not work - minimize()
}

apply(plugin = "zk-sync-build-info")