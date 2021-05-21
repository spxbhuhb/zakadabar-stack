/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*

fun List<BoConstraint>.toCode(): String {
    val parts = mutableListOf<String>()

    forEach {
        parts += when (it) {
            is BooleanBoConstraint -> it.toCode()
            is DoubleBoConstraint -> it.toCode()
            is InstantBoConstraint -> it.toCode()
            is IntBoConstraint -> it.toCode()
            is LongBoConstraint -> it.toCode()
            is StringBoConstraint -> it.toCode()
        }
    }

    return parts.joinToString(" ")
}

fun BooleanBoConstraint.toCode() = "${type.name.toLowerCase()} $value"

fun DoubleBoConstraint.toCode() : String {
    val s = value.toString()
    return "${type.name.toLowerCase()} ${if ('.' in s) s else "$s.0"}"
}

fun InstantBoConstraint.toCode() = "${type.name.toLowerCase()} $value"

fun IntBoConstraint.toCode() = "${type.name.toLowerCase()} $value"

fun LongBoConstraint.toCode() = "${type.name.toLowerCase()} $value"

fun StringBoConstraint.toCode() = "${type.name.toLowerCase()} \"$value\""
