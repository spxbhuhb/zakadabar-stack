/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import io.ktor.application.*
import io.ktor.auth.*
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

fun ApplicationCall.executor() = authentication.principal<KtorExecutor>() !!

class KtorExecutor(
    accountId: EntityId<out BaseBo>,
    anonymous: Boolean,
    roleIds: List<EntityId<out BaseBo>>,
    roleNames: List<String>,
) : Executor(accountId, anonymous, roleIds, roleNames), Principal