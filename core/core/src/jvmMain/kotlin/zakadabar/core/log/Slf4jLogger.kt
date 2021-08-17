/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.log

import org.slf4j.LoggerFactory

open class Slf4jLogger(
    val namespace : String?
) : Logger {

    open val slf4jLogger by lazy<org.slf4j.Logger> { LoggerFactory.getLogger(namespace ?: "inline-object") }

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