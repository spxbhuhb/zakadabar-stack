/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser

import zakadabar.core.authorize.AppRolesBase
import zakadabar.core.authorize.appRoles
import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.ZkApplication
import zakadabar.core.module.modules
import zakadabar.lib.accounts.browser.accounts.Account
import zakadabar.lib.accounts.browser.accounts.AccountSecure
import zakadabar.lib.accounts.browser.login.Login
import zakadabar.lib.accounts.browser.roles.Roles

/**
 * Installs the module. Parameters can be used to override default instances.
 *
 * @param  routing        Routing of the application.
 * @param  login          Login page.
 * @param  account        Account self-management for individual users.
 * @param  accountSecure  Account management CRUD for security officers.
 * @param  roles          Role management CRUD for security officers.
 */
fun install(
    routing: ZkAppRouting,
    login: Login = Login(),
    account: Account = Account(),
    accountSecure: AccountSecure = AccountSecure(),
    roles: Roles = Roles()
) {
    with(routing) {
        + login
        + account
        + accountSecure
        + roles
    }
}

fun install(application: ZkApplication, roles: AppRolesBase = AppRolesBase()) {
    appRoles = roles
    modules += SessionManager()
}
