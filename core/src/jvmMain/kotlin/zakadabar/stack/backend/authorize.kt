/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import zakadabar.stack.util.Executor

fun authorize(executor: Executor, roleName: String) {
    require(executor.hasRole(roleName))
}

fun authorize(executor: Executor, check: (executor: Executor) -> Boolean) {
    require(check(executor))
}