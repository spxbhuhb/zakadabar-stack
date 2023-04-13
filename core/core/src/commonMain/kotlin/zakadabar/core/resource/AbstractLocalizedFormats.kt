/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlinx.datetime.*
import kotlin.math.*

abstract class AbstractLocalizedFormats(
    val config: LocalizationConfig
) : LocalizedFormats {

    // -------------------------------------------------------------------------
    // Int
    // -------------------------------------------------------------------------

    override fun format(value: Int): String {
        return value.toString()
    }

    override fun toInt(value: String): Int {
        return value.toInt()
    }

    override fun toIntOrNull(value: String): Int? {
        return value.toIntOrNull()
    }

    // -------------------------------------------------------------------------
    // Long
    // -------------------------------------------------------------------------

    override fun format(value: Long): String {
        return value.toString()
    }

    override fun toLong(value: String): Long {
        return value.toLong()
    }

    override fun toLongOrNull(value: String): Long? {
        return value.toLongOrNull()
    }

    // -------------------------------------------------------------------------
    // Double
    // -------------------------------------------------------------------------

    override fun format(value: Double): String {
        return value.toString()
    }

    val maxDecimals = 10
    val shifts = Array(maxDecimals) { idx -> 10.0.pow(idx) }

    // TODO replace this with JavaScripts 'Number.toLocaleString'
    override fun format(value: Double, decimals: Int): String {
        check(decimals < maxDecimals) { "decimals must to be less than $maxDecimals" }

        return when {
            value.isNaN() -> "NaN"
            value.isInfinite() -> if (value < 0) "-Inf" else "+Inf"
            decimals == 0 -> {
                round(value).toString().substringBefore('.').withSeparators(config.thousandSeparator).let {
                    if (it == "-0") "0" else it
                }
            }
            else -> {
                val integral = if (value < 0.0) {
                    ceil(value)
                } else {
                    floor(value)
                }

                val decimal = abs(round((value - integral) * shifts[decimals])).toLong().toString().padEnd(decimals, '0')

                return integral.toString().substringBefore('.').withSeparators(" ") + config.decimalSeparator + decimal
            }
        }
    }

    override fun toDouble(value: String): Double {
        return value.toDouble()
    }

    override fun toDoubleOrNull(value: String): Double? {
        return value.toDoubleOrNull()
    }


    // -------------------------------------------------------------------------
    // Instant
    // -------------------------------------------------------------------------

    override fun format(value: Instant): String {
        val local = value.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        return local.substring(0, 10) + " " + local.substring(11, 19)
    }

    override fun toInstant(value: String): Instant {
        return Instant.parse(value)
    }

    override fun toInstantOrNull(value: String): Instant? {
        return try {
            toInstant(value)
        } catch (ex: Exception) {
            null
        }
    }

    // -------------------------------------------------------------------------
    // LocalDate
    // -------------------------------------------------------------------------

    override fun format(value: LocalDate): String {
        return value.toString()
    }

    override fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    override fun toLocalDateOrNull(value: String): LocalDate? {
        return try {
            toLocalDate(value)
        } catch (ex: Exception) {
            null
        }
    }

    // -------------------------------------------------------------------------
    // LocalDateTime
    // -------------------------------------------------------------------------

    override fun format(value: LocalDateTime): String {
        return value.toString().replace('T', ' ')
    }

    override fun toLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }

    override fun toLocalDateTimeOrNull(value: String): LocalDateTime? {
        val fixedValue = value.replace(' ', 'T')
        return try {
            toLocalDateTime(fixedValue)
        } catch (ex: Exception) {
            null
        }
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    fun String.withSeparators(separator: String) =
        reversed()
            .chunked(3)
            .joinToString(separator)
            .reversed()
}