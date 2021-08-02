/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

actual inline fun <T : Any> critical(block: () -> T) {
    // in JavaScript there are no threads,
    // so it is safe to call the function as it is
    block()
}