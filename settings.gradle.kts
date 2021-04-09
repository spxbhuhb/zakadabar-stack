/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
include("demo:demo")
include("demo:demo-lib")
include("demo:demo-jvm-client")
include("lib:markdown")
include("site")