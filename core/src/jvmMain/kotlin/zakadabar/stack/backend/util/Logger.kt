/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

import org.slf4j.Logger

fun Logger.log(lazyMessage: () -> String) {
    if (isInfoEnabled) info(lazyMessage())
}

fun Logger.logUntrusted(lazyMessage: () -> Any?) {
    if (isInfoEnabled) info(lazyMessage()?.toString() ?: "null")
}

fun Logger.fine(lazyMessage: () -> String) {
    if (isTraceEnabled) trace(lazyMessage())
}

fun Logger.fineUntrusted(lazyMessage: () -> Any?) {
    if (isTraceEnabled) trace(lazyMessage()?.toString() ?: "null")
}