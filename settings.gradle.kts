/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "zakadabar-stack"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include("plugin:rui:rui-runtime")
include("plugin:rui:rui-kotlin-plugin")
include("plugin:rui:rui-gradle-plugin")

//include("core:core")
//include("core:core-android")
//
//include("cookbook")
//
//include("exp:content")
//include("exp:liveview")
//
//include("lib:accounts")
//include("lib:bender")
//include("lib:blobs")
//include("lib:email")
//include("lib:examples")
//include("lib:i18n")
//include("lib:lucene")
//include("lib:markdown")
//include("lib:schedule")
//include("lib:softui")
//
//include("demo:demo-basic")
//include("demo:demo-basic-mobile")
//include("demo:demo-content")
//include("demo:demo-sandbox")
//include("demo:demo-sandbox-mobile")
//
//include("site")