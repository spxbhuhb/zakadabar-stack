/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Perform a function that is allowed to do suspending IO. In JavaScript this
 * is not that important as of now.
 */
fun io(func: suspend () -> Unit) = GlobalScope.launch {
    try {
        func()
    } catch (ex: Throwable) {
        // TODO add a function to Application to channel all errors into one place, notify the user, upload the error report, etc.
        log(ex)
    }
}
