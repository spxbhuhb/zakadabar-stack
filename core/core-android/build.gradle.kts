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
    api(project(":core:core"))
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/hk2-locator/default")
        exclude("license/*")
        exclude("license/LICENSE.dom-documentation.txt")
        exclude("META-INF/io.netty.versions.properties")
        exclude("META-INF/INDEX.LIST")
    }
}
