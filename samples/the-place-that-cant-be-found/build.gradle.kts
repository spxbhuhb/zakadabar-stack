/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    application
}

application {
    mainClassName = "zakadabar.stack.backend.app.ServerKt"
}

kotlin {

    jvm {
        withJava()
    }

    js {
        browser()
    }

}
