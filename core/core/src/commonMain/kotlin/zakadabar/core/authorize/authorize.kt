/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.exception.Forbidden
import zakadabar.core.util.PublicApi

@PublicApi
fun authorize(executor: Executor, roleName: String) {
    if (! executor.hasRole(roleName)) throw Forbidden()
}

@PublicApi
fun authorize(executor: Executor, roleId: EntityId<out BaseBo>) {
    if (! executor.hasRole(roleId)) throw Forbidden()
}

@PublicApi
fun authorize(executor: Executor, vararg roleNames: String) {
    if (! executor.hasOneOfRoles(roleNames)) throw Forbidden()
}

@PublicApi
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
fun authorize(authorized: Boolean) {
    if (! authorized) throw Forbidden()
}

