/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor
import zakadabar.stack.util.PublicApi

@PublicApi
fun authorize(executor: Executor, roleName: String) {
    if (! executor.hasRole(roleName)) throw Forbidden()
}

@PublicApi
fun authorize(executor: Executor, roleId: RecordId<RoleDto>) {
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
fun authorize(authorized: Boolean) {
    if (! authorized) throw Forbidden()
}

class Forbidden : Exception()