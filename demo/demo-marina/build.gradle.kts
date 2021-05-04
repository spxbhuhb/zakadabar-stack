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
version = "2021.4.30"

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

val distDir = "$buildDir/${project.name}-$version-server"

val copyAppStruct by tasks.registering(Copy::class) {
    from("$projectDir/template/app")
    into(distDir)
    include("**")
    exclude("**/.gitignore")
}

val copyAppLib by tasks.registering(Copy::class) {
    from("$buildDir/libs")
    into("$distDir/lib")
    include("${base.archivesBaseName}-${project.version}-all.jar")
}

val copyAppIndex by tasks.registering(Copy::class) {
    from("$buildDir/distributions")
    into("$distDir/var/static")
    include("index.html")
    filter { line: String ->
        line.replace("""src="/${project.name}.js"""", """src="/${project.name}-${project.version}.js"""")
    }
}

val copyAppStatic by tasks.registering(Copy::class) {
    from("$buildDir/distributions")
    into("$distDir/var/static")
    include("**")

    exclude("index.html")
    exclude("*.tar")
    exclude("*.zip")

    rename("${project.name}.js", "${project.name}-${project.version}.js")
}

val copyAppUsr by tasks.registering(Copy::class) {
    from("$projectDir")
    into("$distDir/usr")
    include("README.md")
    include("LICENSE.txt")
}

val appDistZip by tasks.registering(Zip::class) {
    dependsOn(copyAppStruct, copyAppLib, copyAppStatic, copyAppIndex, copyAppUsr)

    archiveFileName.set("${base.archivesBaseName}-${project.version}-app.zip")
    destinationDirectory.set(file("$buildDir/app"))

    from("$buildDir/appDist")
}

docker {

    dependsOn(tasks.getByName("appDistZip"))

    name = "spxbhuhb/zakadabar-demo:$version"

    pull(true)
    setDockerfile(file("Dockerfile"))

    copySpec.from(distDir).into("local/{$project.name}")

}