/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import io.ktor.auth.*

/**
 * Ktor authentication principal id.
 *
 * @property  entityId  The id of the Principal entity this principal id points to.
 */
open class Executor internal constructor(
    val entityId: Long
) : Principal