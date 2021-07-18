/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.log

interface Logger {
    fun info(message : String) { println(message) }
    fun warn(message : String) { println(message) }
    fun error(message : String) { println(message) }
}