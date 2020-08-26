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

package zakadabar.stack.frontend.errors

import kotlin.contracts.contract

inline fun ensure(condition: Boolean, lazyMessage: () -> Any) {
    if (! condition) {
        throw handle(lazyMessage())
    }
}

inline fun <T : Any> ensureNull(value: T?, lazyMessage: () -> Any) {
    contract {
        returns() implies (value == null)
    }

    if (value != null) {
        throw handle(lazyMessage())
    }
}

inline fun <T : Any> ensureNotNull(value: T?, lazyMessage: () -> Any): T {
    contract {
        returns() implies (value != null)
    }

    return value ?: throw handle(lazyMessage())
}

// ---- handle -----------------------------------------------------

fun handle(data: Any) =
    when (data) {
        is String -> PreconditionFailed(data)
        is Throwable -> PreconditionFailed(cause = data)
        is Pair<*, *> -> {
            val (first, second) = data
            when {
                first is String && second is String -> PreconditionFailed(first, second)
                first is String && second is Throwable -> PreconditionFailed(first, cause = second)
                else -> PreconditionFailed(data.toString())
            }
        }
        is Triple<*, *, *> -> {
            val (first, second, third) = data
            if (first is String && second is String && third is Throwable) {
                PreconditionFailed(first, second, third)
            } else {
                PreconditionFailed(data.toString())
            }
        }
        else -> PreconditionFailed(data.toString())
    }
