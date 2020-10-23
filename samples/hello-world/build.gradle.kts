/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    application
}

application {
    mainClassName = "zakadabar.samples.hello.world.backend.MainKt"
}

kotlin {

    jvm {
        withJava()
    }

    js {
        browser()
    }

}
