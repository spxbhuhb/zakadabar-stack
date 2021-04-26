/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import kotlinx.datetime.Instant

class SessionCacheEntry(
    val sessionId: String,
    val sessionData: ByteArray,
    val createdAt: Instant
)
