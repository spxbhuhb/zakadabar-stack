/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    `kotlin-dsl`
    id("com.palantir.docker") version "0.25.0"
    signing
    `maven-publish`
}

repositories {
    mavenCentral()
}