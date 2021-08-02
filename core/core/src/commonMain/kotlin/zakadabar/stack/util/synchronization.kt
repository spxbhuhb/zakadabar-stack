/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

/**
 * Common version of `synchronized` as it is going to be deprecated and Mutex
 * of kotlinx.serialization is really-really an overkill.
 */
expect inline fun <T : Any> critical(block : () -> T)