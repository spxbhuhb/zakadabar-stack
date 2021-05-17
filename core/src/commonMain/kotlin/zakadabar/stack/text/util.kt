/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.text

fun String.camelToSnakeCase(): String {
    var s = ""
    this.toCharArray().forEach {
        s += if (it in 'A'..'Z') "_${it.toLowerCase()}" else it.toString()
    }
    return s.trimStart('_')
}