/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
version = "2021.3.26-SNAPSHOTs"

application {
    mainClassName = "zakadabar.stack.backend.ServerKt"
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
    }
}


tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    // seems like this does not work - minimize()
}

val distName = "demo-$version-server"

val copyAppStruct by tasks.registering(Copy::class) {
    from("$projectDir/app")
    into("$buildDir/$distName")
    include("**")
    exclude("**/.gitignore")
}

val copyAppLib by tasks.registering(Copy::class) {
    from("$buildDir/libs")
    into("$buildDir/$distName/lib")
    include("${base.archivesBaseName}-${project.version}-all.jar")
}

val copyAppStatic by tasks.registering(Copy::class) {
    from("$buildDir/distributions")
    into("$buildDir/$distName/var/static")
    include("**")
    exclude("*.tar")
    exclude("*.zip")
}

val copyAppUsr by tasks.registering(Copy::class) {
    from("$projectDir")
    into("$buildDir/$distName/usr")
    include("README.md")
    include("LICENSE.txt")
}

val appDistZip by tasks.registering(Zip::class) {
    dependsOn(copyAppStruct, copyAppLib, copyAppStatic, copyAppUsr)

    archiveFileName.set("${base.archivesBaseName}-${project.version}-app.zip")
    destinationDirectory.set(file("$buildDir/app"))

    from("$buildDir/appDist")
}