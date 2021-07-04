/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend

import zakadabar.lib.accounts.frontend.accounts.Account
import zakadabar.lib.accounts.frontend.accounts.AccountSecure
import zakadabar.lib.accounts.frontend.login.Login
import zakadabar.lib.accounts.frontend.roles.Roles
import zakadabar.stack.authorize.AppRolesBase
import zakadabar.stack.authorize.appRoles
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication

fun install(routing: ZkAppRouting) {
    with(routing) {
        + AccountSecure()
        + Account()
        + Roles()
        + Login()
    }
}

fun install(application : ZkApplication, roles : AppRolesBase = AppRolesBase()) {
    appRoles = roles
    application.services += SessionManager()
}
