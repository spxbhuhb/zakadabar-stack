/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlinx.datetime.*

class BuiltinLocalizedFormats : LocalizedFormats {

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
        } catch (ex : Exception) {
            null
        }
    }

    override fun format(value: LocalDate): String {
        return value.toString()
    }

    override fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    override fun toLocalDateOrNull(value: String): LocalDate? {
        return try {
            toLocalDate(value)
        } catch (ex : Exception) {
            null
        }
    }

    override fun format(value: LocalDateTime): String {
        return value.toString()
    }

    override fun toLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }

    override fun toLocalDateTimeOrNull(value: String): LocalDateTime? {
        return try {
            toLocalDateTime(value)
        } catch (ex : Exception) {
            null
        }
    }

}