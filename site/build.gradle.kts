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
version = "2021.6.15"

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
        implementation(project(":lib:accounts"))
        implementation(project(":lib:examples"))
        implementation(project(":lib:bender"))
        implementation(project(":lib:markdown"))
    }
    
    sourceSets["jvmMain"].dependencies {
        implementation("com.h2database:h2:1.4.200")
    }
}


tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    // seems like this does not work - minimize()
}

val syncBuildInfo by tasks.registering(Sync::class) {
    from("$projectDir/template/zkBuild")
    inputs.property("version", project.version)
    filter { line: String ->
        line.replace("@version@", "${project.version}")
            .replace("@projectName@", project.name)
            .replace("@stackVersion@", "${rootProject.childProjects["core"]?.version ?: "unknown"}")
    }
    into("$projectDir/src/jvmMain/resources")
}

tasks["compileKotlinJvm"].dependsOn(syncBuildInfo)

val distDir = "$buildDir/${project.name}-$version-server"

val copyAppStruct by tasks.registering(Copy::class) {
    from("$projectDir/template/app")
    into(distDir)
    include("**")
    exclude("**/.gitignore")

    filter { line: String ->
        line.replace("@version@", "${project.version}")
    }
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

val copyMarkdown by tasks.registering(Copy::class) {
    from("${rootProject.projectDir}/core/doc")
    into("$distDir/var/static/doc")
    include("**/*")
    includeEmptyDirs = false
}

val copyAppUsr by tasks.registering(Copy::class) {
    from("$projectDir")
    into("$distDir/usr")
    include("README.md")
    include("LICENSE.txt")
}

val appDistZip by tasks.registering(Zip::class) {
    dependsOn(copyAppStruct, copyAppLib, copyAppStatic, copyAppIndex, copyMarkdown, copyAppUsr)

    archiveFileName.set("${base.archivesBaseName}-${project.version}-app.zip")
    destinationDirectory.set(file("$buildDir/app"))

    from("$buildDir/appDist")
}