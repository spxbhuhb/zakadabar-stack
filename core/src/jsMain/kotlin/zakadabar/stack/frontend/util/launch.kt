/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A general wrapper around launched suspend functions to avoid the repetition.
 */
fun launch(func: suspend () -> Unit) {
    GlobalScope.launch {
        try {
            func()
        } catch (ex: Throwable) {
            log(ex)
        }
    }
}