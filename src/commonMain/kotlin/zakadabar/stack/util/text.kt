/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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