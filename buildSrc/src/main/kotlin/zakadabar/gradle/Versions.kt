/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.gradle

object Versions {
    const val zakadabar = "2022.4.25"

    val isSnapshot = zakadabar.contains("SNAPSHOT")

    const val kotlin = "1.6.20"
    const val ktor = "1.6.8"
    const val coroutines = "1.5.2"
    const val serialization = "1.3.1"
    const val datetime = "0.2.1"
    const val exposed = "0.31.1"

    const val clikt = "3.2.0"
    const val kaml = "0.19.0"

    const val hikari = "3.4.5"
    const val postgresql = "42.2.14"
    const val h2 = "1.4.200"
    const val sqliteJdbc = "3.36.0.1"

    const val javamail = "1.6.2"

    const val highlightJs = "10.7.2"
    const val markdown = "0.2.4"

    object Android {
        const val compileSdk = 31
        const val minSdk = 26
        const val targetSdk = 31
        val versionCode = zakadabar.toVersionCode()
        const val versionName = zakadabar
    }

    fun String.toVersionCode() : Int {
        val e = this.substringBefore("-").split(".")
        val sub = if (e.size == 3) 0 else e[3].toInt()
        check(sub < 100) { "cannot have sub version larger than 10"}
        return e[0].toInt() * 1000000 + e[1].toInt() + 10000 + e[2].toInt() * 100 + sub
    }
}