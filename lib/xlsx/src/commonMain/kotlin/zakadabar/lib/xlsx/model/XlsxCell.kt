/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import kotlinx.datetime.*

/**
 * Cell object, value and format holder.
 */
class XlsxCell internal constructor(
    val sheet : XlsxSheet,
    val coordinate: XlsxCoordinate
) {

    /**
     * Value in cell
     * Recognized types: Boolean, Number, Enum, LocalDate, LocalDateTime, Instant, String.
     *
     * Other types converted into String by toString() method.
     */
    var value: Any? = null
        set(v) {
            field = v
            numberFormat = calcNumberFormat()
        }

    /**
     * Nummeric format in the cell
     *
     * Customizable via Configuration object
     *
     * set automatic by value
     */
    lateinit var numberFormat: XlsxCellFormat

    private fun calcNumberFormat() : XlsxCellFormat {

        val conf = sheet.doc.conf

        return when(value) {
            is LocalDate -> conf.dateFormat
            is LocalDateTime -> conf.dateTimeFormat
            is Instant -> conf.instantFormat
            else -> conf.formats.GENERAL
        }
    }

    /**
     * String representation of a cell
     */
    override fun toString() : String = asString() ?: ""

    /**
     * like toString() but when cell is empty, returns null
     */
    fun asString() : String? = when(val v = value) {
        null -> null
        is String -> v
        else -> v.toString()
    }

    /**
     * extract Boolean value
     * null if empty
     * try to parse in case of String type
     * @throws NumberFormatException when cell is not a boolean cell
     */
    fun asBoolean() : Boolean? = when(val v = value) {
        null -> null
        is Boolean -> v
        is Number -> v.toInt() == 1
        is String -> v.toBoolean() || v == "1"
        else -> throw NumberFormatException("$coordinate is not a boolean")
    }

    /**
     * extract Long value
     * null if empty
     * try to parse in case of String type
     * @throws NumberFormatException when cell is not a number
     */
    fun asLong() : Long? = when(val v = value) {
        null -> null
        is Long -> v
        is Number -> v.toLong()
        is String -> v.toLong()
        is Boolean -> if (v) 1L else 0L
        else -> throw NumberFormatException("$coordinate is not a number")
    }

    /**
     * extract Double value
     * null if empty
     * try to parse in case of String type
     * @throws NumberFormatException when cell is not a number
     */
    fun asDouble() : Double? = when(val v = value) {
        null -> null
        is Double -> v
        is Number -> v.toDouble()
        is String -> v.toDouble()
        is Boolean -> if (v) 1.0 else 0.0
        else -> throw NumberFormatException("$coordinate is not a number")
    }

    /**
     * extract LocalDate value
     * null if empty
     * try to parse in case of String type
     * @throws NumberFormatException when cell is not a date
     */
    fun asDate() : LocalDate? = when (val v = value) {
        null -> null
        is LocalDate -> v
        is LocalDateTime -> LocalDate(v.year, v.monthNumber, v.dayOfMonth)
        is String -> LocalDate.parse(v)
        else -> throw NumberFormatException("$coordinate is not a date")
    }

    /**
     * extract LocalDateTime value
     * null if empty
     * try to parse in case of String type
     * @throws NumberFormatException when cell is not a date
     */
    fun asDateTime() : LocalDateTime? = when (val v = value) {
        null -> null
        is LocalDateTime -> v
        is LocalDate -> LocalDateTime(v.year, v.monthNumber, v.dayOfMonth, 0, 0, 0)
        is String -> LocalDateTime.parse(v)
        else -> throw NumberFormatException("$coordinate is not a date")
    }

}