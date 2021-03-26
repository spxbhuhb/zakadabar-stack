/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform") version "1.4.31" apply false
    kotlin("plugin.serialization") version "1.4.31" apply false
    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.31" apply false
    id("org.jetbrains.dokka") version "1.4.30" apply false
    id("com.github.johnrengelman.shadow") version "6.0.0" apply false
    signing
    `maven-publish`
}

subprojects {

    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx/") // for kotlinx.datetime, to be changed to jcenter()
    }

}
