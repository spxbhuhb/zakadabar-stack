/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.log

open class Slf4jLogger(
    open val slf4jLogger: org.slf4j.Logger
) : Logger {

    override fun info(message : String) {
        slf4jLogger.info(message)
    }

    override fun warn(message : String) {
        slf4jLogger.warn(message)
    }

    override fun error(message : String) {
        slf4jLogger.error(message)
    }

    override fun error(message : String, ex : Throwable) {
        slf4jLogger.error(message, ex)
    }

    override fun debug(message: String) {
        slf4jLogger.debug(message)
    }
}