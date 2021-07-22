/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "zakadabar-stack"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include("core:core")
//include("core:core-android")

include("cookbook")

//include("exp:content")
//include("exp:email")
//include("exp:scheduler")
//
include("lib:accounts")
//include("lib:bender")
include("lib:blobs")
include("lib:examples")
include("lib:i18n")
include("lib:markdown")
//
include("demo:demo-basic")
//include("demo:demo-basic-mobile")
//include("demo:demo-content")
//include("demo:demo-sandbox")
//include("demo:demo-sandbox-mobile")
//
//include("site")

//include("upgrade:2021-6-to-2021-7")
