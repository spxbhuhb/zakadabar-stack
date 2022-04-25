/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform") version "1.6.20" apply false
    kotlin("plugin.serialization") version "1.6.20" apply false
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.20" apply false
    id("org.jetbrains.dokka") version "1.4.32" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    signing
    `maven-publish`
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
        classpath("com.android.tools.build:gradle:7.0.4")
    }
}

subprojects {

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

}
