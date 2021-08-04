/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

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
            is CustomBoConstraint -> it.toCode()
            is LocalDateBoConstraint -> it.toCode()
            is LocalDateTimeBoConstraint -> it.toCode()
        }
    }

    return parts.joinToString(" ")
}

fun BooleanBoConstraint.toCode() = "${constraintType.name.lowercase()} $value"

fun CustomBoConstraint.toCode() : String {
    return "custom(\"${name}\") { ${value ?: ""} }"
}

fun DoubleBoConstraint.toCode() : String {
    val s = value.toString()
    return "${constraintType.name.lowercase()} ${if ('.' in s) s else "$s.0"}"
}

fun InstantBoConstraint.toCode() = "${constraintType.name.lowercase()} $value"

fun LocalDateBoConstraint.toCode() = "${constraintType.name.lowercase()} $value"

fun LocalDateTimeBoConstraint.toCode() = "${constraintType.name.lowercase()} $value"

fun IntBoConstraint.toCode() = "${constraintType.name.lowercase()} $value"

fun LongBoConstraint.toCode() = "${constraintType.name.lowercase()} $value"

fun StringBoConstraint.toCode() = "${constraintType.name.lowercase()} \"$value\""
