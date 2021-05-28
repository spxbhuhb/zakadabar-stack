/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.util.PublicApi

@PublicApi
@Deprecated("use Authorizer (preferred) or the one from zakadbar.stack.backend.authorize")
fun authorize(executor: Executor, roleName: String) {
    if (! executor.hasRole(roleName)) throw Forbidden()
}

@PublicApi
@Deprecated("use Authorizer (preferred) or the one from zakadbar.stack.backend.authorize")
fun authorize(executor: Executor, vararg roleNames: String) {
    if (! executor.hasOneOfRoles(roleNames)) throw Forbidden()
}

@PublicApi
@Deprecated("use Authorizer (preferred) or the one from zakadbar.stack.backend.authorize")
fun authorize(executor: Executor, check: (executor: Executor) -> Boolean) {
    if (! check(executor)) throw Forbidden()
}

@PublicApi
/**
 * Authorize directly (without role, check, etc).
 *
 * @param  authorized  When true, the request is authorized, when false it is denied.
 *
 * @throws [Forbidden]
 */
@Deprecated("use the one from zakadbar.stack.backend.authorize")
fun authorize(authorized: Boolean) {
    if (! authorized) throw Forbidden()
}

@Deprecated("use the one from zakadbar.stack.backend.authorize")
class Forbidden : Exception()