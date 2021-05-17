/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*

fun List<ConstraintDto>.toCode(): String {
    val parts = mutableListOf<String>()

    forEach {
        parts += when (it) {
            is ConstraintBooleanDto -> it.toCode()
            is ConstraintDoubleDto -> it.toCode()
            is ConstraintInstantDto -> it.toCode()
            is ConstraintIntDto -> it.toCode()
            is ConstraintLongDto -> it.toCode()
            is ConstraintStringDto -> it.toCode()
        }
    }

    return parts.joinToString(" ")
}

fun ConstraintBooleanDto.toCode() = "${type.name.toLowerCase()} $value"

fun ConstraintDoubleDto.toCode() : String {
    val s = value.toString()
    return "${type.name.toLowerCase()} ${if ('.' in s) s else "$s.0"}"
}

fun ConstraintInstantDto.toCode() = "${type.name.toLowerCase()} $value"

fun ConstraintIntDto.toCode() = "${type.name.toLowerCase()} $value"

fun ConstraintLongDto.toCode() = "${type.name.toLowerCase()} $value"

fun ConstraintStringDto.toCode() = "${type.name.toLowerCase()} \"$value\""
