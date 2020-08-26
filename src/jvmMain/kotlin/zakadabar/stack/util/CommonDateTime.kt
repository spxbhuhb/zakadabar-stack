/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.util

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

actual class CommonDateTime(source: OffsetDateTime) {

    private val value = source

    actual fun millisSinceEpoch() = value.toEpochSecond() * 1000L + value.nano / 1_000_000L

    actual fun asFileTimeStamp(): String {
        return value.toString().replace('T', '_').replace(':', '_').substringBefore('.')
    }

    actual companion object {
        actual fun now() = CommonDateTime(OffsetDateTime.now())

        actual fun fromMillis(millis: Long) =
            CommonDateTime(OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC))
    }

}