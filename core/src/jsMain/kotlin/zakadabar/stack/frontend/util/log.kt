/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

fun log(ex: dynamic): String {
    if (ex == null) return ""

    val stack = ex.stack
    return if (stack is String) {
        val message = ex.message?.toString() ?: ""
        val error = js("Error()")
        error.name = ex.toString().substringBefore(':')
        error.message = message.substringAfter(':')
        error.stack = stack
        @Suppress("UnsafeCastFromDynamic") // magic, this whole function is pure magic
        console.error(error)
        error.toString()
    } else {
        console.log(ex)
        ex.toString()
    }
}