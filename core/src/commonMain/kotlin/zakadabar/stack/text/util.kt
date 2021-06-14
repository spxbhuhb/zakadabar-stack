/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.text

fun String.capitalized() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun String.camelToSnakeCase(): String {
    var s = ""
    this.toCharArray().forEach {
        s += if (it in 'A'..'Z') "_${it.lowercaseChar()}" else it.toString()
    }
    return s.trimStart('_')
}

fun String.lowercaseWithHyphen(): String {
    val chars = this.toCharArray()
    chars.forEachIndexed { index, char ->
        chars[index] = if (char.isWhitespace()) '-' else char.lowercaseChar()
    }
    return chars.concatToString()
}