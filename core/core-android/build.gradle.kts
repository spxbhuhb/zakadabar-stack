/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.dependencies.Versions

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

dependencies {
    api(project(":core:core-core"))
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
}
