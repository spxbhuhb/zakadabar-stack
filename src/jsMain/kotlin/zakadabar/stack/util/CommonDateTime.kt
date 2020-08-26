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

import kotlin.js.Date

actual class CommonDateTime {

    private val value: Date

    actual override fun toString() = value.toISOString()

    actual fun asFileTimeStamp(): String {
        return value.toISOString().replace('T', '_').replace(':', '_').substringBefore('.')
    }

    actual fun millisSinceEpoch() = value.getTime().toLong()

    actual companion object {
        actual fun now() = CommonDateTime(Date())

        actual fun fromMillis(millis: Long) = CommonDateTime(millis)

    }

    constructor(date: Date) {
        value = date
    }

    constructor(millis: Long) {
        value = Date(millis)
    }

}