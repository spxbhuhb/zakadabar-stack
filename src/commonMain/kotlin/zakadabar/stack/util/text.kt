/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

private val regex = Regex("[\\n\\r\" ]")

fun toQuoted(value: String): String {
    if (! value.contains(regex)) return value

    val escaped = value
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")

    return "\"${escaped}\""
}

fun splitQuoted(value: String): List<String> {
    val args = mutableListOf<String>()

    var inQuoted = false
    var escaped = false

    val chars = mutableListOf<Char>()

    loop@ for (char in value) {
        when {
            char == ' ' && ! inQuoted -> {
                if (chars.isEmpty()) continue@loop
                args += chars.toCharArray().concatToString()
                chars.clear()
            }

            char == '\"' && ! inQuoted -> {
                if (chars.isEmpty()) {
                    inQuoted = true
                } else {
                    chars += char
                }
            }

            char == '"' && inQuoted -> {
                if (escaped) {
                    chars += char
                    escaped = false
                } else {
                    inQuoted = false
                    args += chars.toCharArray().concatToString()
                    chars.clear()
                }
            }

            char == '\\' && inQuoted -> {
                if (escaped) {
                    chars += char
                    escaped = false
                } else {
                    escaped = true
                }
            }

            else -> {
                if (escaped) {
                    when (char) {
                        'n' -> chars += '\n'
                        'r' -> chars += '\r'
                        else -> {
                            chars += '\\'
                            chars += char
                        }
                    }
                    escaped = false
                } else {
                    chars += char
                }
            }
        }
    }

    if (chars.isNotEmpty()) {
        args += chars.toCharArray().concatToString()
        chars.clear()
    }

    return args
}