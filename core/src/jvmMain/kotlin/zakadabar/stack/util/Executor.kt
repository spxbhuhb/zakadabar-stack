/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import io.ktor.auth.*
import zakadabar.stack.backend.Server

/**
 * Ktor authentication principal id.
 *
 * @property  accountId  The id of the Principal entity this principal id points to.
 */
open class Executor internal constructor(

    val accountId: Long

) : Principal {

    val isAnonymous
        get() = accountId == Server.anonymous.id

}