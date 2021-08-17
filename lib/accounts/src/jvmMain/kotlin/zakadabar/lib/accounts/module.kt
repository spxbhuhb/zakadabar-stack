/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts

import zakadabar.core.authorize.AppRolesBase
import zakadabar.core.authorize.appRoles
import zakadabar.core.server.server
import zakadabar.lib.accounts.business.AccountPrivateBl
import zakadabar.lib.accounts.business.KtorSessionBl
import zakadabar.lib.accounts.business.RoleBl

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