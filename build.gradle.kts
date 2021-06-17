/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform") version "1.5.0" apply false
    kotlin("plugin.serialization") version "1.5.0" apply false
    id("org.jetbrains.kotlin.plugin.noarg") version "1.5.0" apply false
    id("org.jetbrains.dokka") version "1.4.32" apply false
    id("com.github.johnrengelman.shadow") version "6.0.0" apply false
    signing
    `maven-publish`
}

buildscript {
    extra["stackVersion"] = "2021.7.1-SNAPSHOT"

    extra["kotlinVersion"] = "1.5.0"
    extra["ktorVersion"] = "1.4.3"
    extra["coroutinesVersion"] = "1.3.9"
    extra["serializationVersion"] = "1.0.0-RC2"
    extra["datetimeVersion"] = "0.2.0"
}

subprojects {

    repositories {
        mavenCentral()
        mavenLocal() // temporary, for markdown snapshot
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx/") // for kotlinx.datetime, TODO remove when ready
        maven(url = "http://dl.bintray.com/jetbrains/markdown") // TODO remove when ready
    }

}
