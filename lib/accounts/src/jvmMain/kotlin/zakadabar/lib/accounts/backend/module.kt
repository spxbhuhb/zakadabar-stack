/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import zakadabar.lib.accounts.backend.session.KtorSessionBl
import zakadabar.stack.RolesBase
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.server

/**
 * @param   roles  Hard-coded roles this application handles. During application
 *                 startup missing roles are added automatically.
 */
fun install(roles : RolesBase = RolesBase()) {

    StackRoles = roles

    server += AccountPrivateBl()
    server += RoleBl()
    server += KtorSessionBl()

}