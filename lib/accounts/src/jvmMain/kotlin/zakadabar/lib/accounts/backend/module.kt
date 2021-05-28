/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import zakadabar.stack.backend.server

fun install() {
    server += AccountPrivateBl()
    server += RoleBl()
    server += RoleGrantBl()
}