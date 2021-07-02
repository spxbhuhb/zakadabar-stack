/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import zakadabar.stack.authorize.AppRolesBase


object Roles : AppRolesBase() {
    val myRole by "my-role"
}