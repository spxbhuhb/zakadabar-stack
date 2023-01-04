/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import kotlinx.datetime.*

/*
 * Useful dat conversion utils handling excel's date store format
 * a date sore in excel:
 * whole part: elapsed days from 1900-01-01
 * fraction part: fraction of a day
 * excel not storing timezone data, so every dat act LocalDate/Time
 */



private val timeBase = LocalDate(1900, 1, 1)

// excel drives me mad. thinks 1900-02-29 is a valid date, but actually it's not
// I wonder what happened that day?
// origin of this was a bug in Lotus-1-2-3
private val EXCEL_1900_LEAP_YEAR_BUG_DATE = LocalDate(1900, 3, 1)

internal fun LocalDate.toInternal() : Int {
    val adjust = if (this < EXCEL_1900_LEAP_YEAR_BUG_DATE) 1 else 2
    return timeBase.daysUntil(this) + adjust
}

internal fun LocalDateTime.toInternal() : Double {
    val datePart = LocalDate(year, monthNumber, dayOfMonth).toInternal()
    val timePart = (3600 * hour + 60 * minute + second + 1e-9 * nanosecond) / 86400
    return datePart + timePart
}

internal fun Instant.toInternal(tz: TimeZone) : Double = toLocalDateTime(tz).toInternal()