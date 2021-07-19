/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

val stdoutTraceInclude = mutableListOf<Regex>()

fun stdoutTrace(tag: String, message: String) {
    if (! include(tag)) return
    println("[ ${tag.padEnd(25).take(25)} ] $message")
}

fun include(tag: String): Boolean {
    if (stdoutTraceInclude.isEmpty()) return true

    for (inc in stdoutTraceInclude) {
        if (inc.matches(tag)) return true
    }

    return false
}
