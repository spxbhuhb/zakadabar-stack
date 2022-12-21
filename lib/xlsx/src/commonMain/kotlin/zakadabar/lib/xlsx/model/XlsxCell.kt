/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import kotlinx.datetime.*
import zakadabar.lib.xlsx.XlsxCellFormat

class XlsxCell(val coordinate: XlsxCoordinate) {

    var value: Any? = null
        set(v) {
            field = v
            format = calcFormat()
        }

    var format: XlsxCellFormat = XlsxCellFormat.GENERAL

    private fun calcFormat() : XlsxCellFormat = when(value) {
        is String -> XlsxCellFormat.GENERAL
        is Boolean -> XlsxCellFormat.GENERAL
        is Number -> XlsxCellFormat.GENERAL
        is LocalDate -> XlsxCellFormat.DATE
        is LocalDateTime -> XlsxCellFormat.DATETIME
        is Instant -> XlsxCellFormat.TIMESTAMP
        else -> XlsxCellFormat.GENERAL
    }

    override fun toString() : String = asString() ?: ""

    fun isEmpty() : Boolean = value == null

    fun isNotEmpty() = !isEmpty()

    fun asString() : String? = when(val v = value) {
        null -> null
        is String -> v
        else -> v.toString()
    }

    fun asBoolean() : Boolean? = when(val v = value) {
        null -> null
        is Boolean -> v
        is Number -> v.toInt() == 1
        is String -> v.toBoolean() || v == "1"
        else -> throw NumberFormatException("$coordinate is not a boolean")
    }

    fun asLong() : Long? = when(val v = value) {
        null -> null
        is Long -> v
        is Number -> v.toLong()
        is String -> v.toLong()
        is Boolean -> if (v) 1L else 0L
        else -> throw NumberFormatException("$coordinate is not a number")
    }

    fun asDouble() : Double? = when(val v = value) {
        null -> null
        is Double -> v
        is Number -> v.toDouble()
        is String -> v.toDouble()
        is Boolean -> if (v) 1.0 else 0.0
        else -> throw NumberFormatException("$coordinate is not a number")
    }

    fun asDate() : LocalDate? = when (val v = value) {
        null -> null
        is LocalDate -> v
        is LocalDateTime -> LocalDate(v.year, v.monthNumber, v.dayOfMonth)
        is String -> LocalDate.parse(v)
        else -> throw NumberFormatException("$coordinate is not a date")
    }

    fun asDateTime() : LocalDateTime? = when (val v = value) {
        null -> null
        is LocalDateTime -> v
        is LocalDate -> LocalDateTime(v.year, v.monthNumber, v.dayOfMonth, 0, 0, 0)
        is String -> LocalDateTime.parse(v)
        else -> throw NumberFormatException("$coordinate is not a date")
    }

}