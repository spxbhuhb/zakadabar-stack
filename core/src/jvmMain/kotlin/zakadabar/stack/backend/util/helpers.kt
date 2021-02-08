/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

import io.ktor.application.*
import io.ktor.auth.*
import zakadabar.stack.util.Executor

fun ApplicationCall.executor() = authentication.principal<Executor>() !!

//fun ApplicationCall.executor() : Executor {
//    val session = sessions.get<StackSession>() ?: throw IllegalStateException("missing session")
//    return Executor(session.account, session.roles)
//}
