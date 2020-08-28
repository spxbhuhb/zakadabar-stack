/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
