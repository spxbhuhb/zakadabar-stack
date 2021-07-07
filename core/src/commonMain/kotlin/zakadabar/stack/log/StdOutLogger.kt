/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.log

class StdOutLogger : Logger {
    override fun info(message : String) { println(message) }
    override fun warn(message : String) { println(message) }
    override fun error(message : String) { println(message) }
}