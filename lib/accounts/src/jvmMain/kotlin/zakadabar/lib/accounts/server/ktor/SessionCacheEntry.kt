/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.server.ktor

import kotlinx.datetime.Instant

class SessionCacheEntry(
    val sessionId: String,
    val sessionData: String,
    val createdAt: Instant
)
