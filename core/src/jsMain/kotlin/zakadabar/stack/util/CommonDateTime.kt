/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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