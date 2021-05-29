/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend

import zakadabar.lib.accounts.frontend.accounts.Accounts
import zakadabar.lib.accounts.frontend.login.Login
import zakadabar.lib.accounts.frontend.roles.Roles
import zakadabar.stack.frontend.application.ZkAppRouting

fun install(routing: ZkAppRouting) {
    with(routing) {
        + Accounts()
        + Roles()
        + Login()
    }
}