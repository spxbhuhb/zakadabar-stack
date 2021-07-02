/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "zakadabar-stack"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

include("core")

include("exp:content")
include("exp:email")
include("exp:scheduler")

include("lib:accounts")
include("lib:bender")
include("lib:blobs")
include("lib:demo")
include("lib:examples")
include("lib:i18n")
include("lib:markdown")

include("site")