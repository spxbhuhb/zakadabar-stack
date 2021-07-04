/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import zakadabar.lib.accounts.backend.bl.AccountPrivateBl
import zakadabar.lib.accounts.backend.bl.KtorSessionBl
import zakadabar.lib.accounts.backend.bl.RoleBl
import zakadabar.stack.authorize.AppRolesBase
import zakadabar.stack.authorize.appRoles
import zakadabar.stack.backend.server

/**
 * @param   roles  Hard-coded roles this application handles. During application
 *                 startup missing roles are added automatically.
 * @param   accountPrivateBl  The BL instance that provides the account business
 *                            logic. Defaults to a new [AccountPrivateBl] instance.
 */
fun install(
    roles: AppRolesBase = AppRolesBase(),
    accountPrivateBl: AccountPrivateBl = AccountPrivateBl()
) {

    appRoles = roles

    server += accountPrivateBl
    server += RoleBl()
    server += KtorSessionBl()

}