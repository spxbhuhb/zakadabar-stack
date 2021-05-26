/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize.roleBl

lateinit var accountPrivateBl : AccountPrivateBl

fun install() {
    Server += PrincipalBl()
    Server += AccountPrivateBl()
    Server += RoleBl().also { roleBl = it }
    Server += RoleGrantBl()
}