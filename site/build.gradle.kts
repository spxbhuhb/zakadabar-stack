/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.dependencies.Versions

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

    id("zk-sync-build-info") apply false
    id("site-compose-app") apply false
    id("site-docker") apply false
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

application {
    mainClass.set("zakadabar.stack.backend.ServerKt")
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
        implementation(project(":core:core"))
        implementation(project(":lib:accounts"))
        implementation(project(":lib:examples"))
        implementation(project(":lib:bender"))
        implementation(project(":lib:markdown"))
        implementation(project(":cookbook"))
    }
    
    sourceSets["jvmMain"].dependencies {
        implementation("com.h2database:h2:${Versions.h2}")
    }
}


tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    // seems like this does not work - minimize()
}

apply(plugin = "zk-sync-build-info")
apply(plugin = "site-compose-app")
apply(plugin = "site-docker")

docker {

    dependsOn(tasks["zkBuild"], tasks["zkDockerPrepare"], tasks["zkDockerCopy"])

    name = project.name
    // this throws unsupported operation exception -- tags.add(version.toString())

    pull(true)
    setDockerfile(file("Dockerfile"))

}

val zkDocker by tasks.creating(Task::class) {
    group = "zakadabar"
    dependsOn(tasks.getByName("docker"))
}
