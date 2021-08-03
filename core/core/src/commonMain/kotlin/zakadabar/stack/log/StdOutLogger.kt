/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.log

import kotlinx.datetime.Clock

open class StdOutLogger : Logger {

    open val now = Clock.System.now().toString().replace('T', ' ')

    override fun info(message : String) {
        println("$now [INFO ]  $message")
    }

    override fun warn(message : String) {
        println("$now [WARN ]  $message")
    }

    override fun error(message : String) {
        println("$now [ERROR]  $message")
    }

    override fun error(message : String, ex : Throwable) {
        println("$now [ERROR]  $message")
        ex.printStackTrace()
    }

    override fun debug(message : String) {
        println("$now [DEBUG]  $message")
    }
}