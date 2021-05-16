/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "zakadabar-stack"

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
    }
}

include("core")
include("lib:cards")
include("lib:kodomat")
include("lib:examples")
include("lib:markdown")
include("site")