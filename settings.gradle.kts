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

include("samples:hello-world")
include("samples:5-ways-to-html")
include("samples:my-life-my-theme-my-style")
include("samples:witches-brew")
include("samples:the-old-man-from-Scene-24")
include("samples:the-place-that-cant-be-found")
include("samples:the-perfect-form")