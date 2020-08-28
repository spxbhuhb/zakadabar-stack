/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

fun log(ex: Throwable?): String {
    if (ex == null) return ""

    val stack = ex.asDynamic().stack
    return if (stack is String) {
        val error = js("Error()")
        error.name = ex.toString().substringBefore(':')
        error.message = ex.message?.substringAfter(':')
        error.stack = stack
        @Suppress("UnsafeCastFromDynamic") // magic, this whole function is pure magic
        console.error(error)
        error.toString()
    } else {
        console.log(ex)
        ex.toString()
    }
}