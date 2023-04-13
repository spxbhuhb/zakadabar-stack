/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface LocalizedFormats {

    fun format(value: Int): String
    fun toInt(value: String): Int
    fun toIntOrNull(value: String): Int?

    fun format(value: Long): String
    fun toLong(value: String): Long
    fun toLongOrNull(value: String): Long?

    fun format(value: Double): String
    fun format(value: Double, decimals: Int): String
    fun toDouble(value: String): Double
    fun toDoubleOrNull(value: String): Double?

    fun format(value: Instant): String
    fun toInstant(value: String): Instant
    fun toInstantOrNull(value: String): Instant?

    fun format(value: LocalDate): String
    fun toLocalDate(value: String): LocalDate
    fun toLocalDateOrNull(value: String): LocalDate?

    fun format(value: LocalDateTime): String
    fun toLocalDateTime(value: String): LocalDateTime
    fun toLocalDateTimeOrNull(value: String): LocalDateTime?

}