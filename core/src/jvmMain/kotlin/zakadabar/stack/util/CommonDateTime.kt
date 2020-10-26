/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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