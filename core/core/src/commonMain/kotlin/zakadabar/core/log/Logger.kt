/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.log

interface Logger {
    fun info(message : String)
    fun warn(message : String)
    fun error(message : String)
    fun error(message : String, ex : Throwable)
    fun debug(message : String)
}