/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources

import kotlinx.datetime.*

object ZkFormatters {

    fun formatInstant(value: Instant): String {
        val local = value.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        return local.substring(0, 10) + " " + local.substring(11, 19)
    }

    fun parseInstant(value: String): Instant {
        return Instant.parse(value)
    }

    fun formatLocalDate(value: LocalDate): String {
        return value.toString()
    }

    fun parseLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }


    fun formatLocalDateTime(value: LocalDate): String {
        return value.toString()
    }

    fun parseLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
}