/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.gradle.dependencies

object Versions {
    const val zakadabar = "2021.8.1-SNAPSHOT"

    val isSnapshot = zakadabar.contains("SNAPSHOT")

    const val kotlin = "1.5.20"
    const val ktor = "1.6.1"
    const val coroutines = "1.5.0"
    const val serialization = "1.2.1"
    const val datetime = "0.2.1"
    const val exposed = "0.31.1"

    const val clikt = "2.8.0"
    const val postgresql = "42.2.14"
    const val hikari = "3.4.5"
    const val kaml = "0.19.0"
    const val h2 = "1.4.200"

    const val javamail = "1.5.0-b01"

    const val highlightJs = "10.7.2"
    const val markdown = "0.2.4"

}