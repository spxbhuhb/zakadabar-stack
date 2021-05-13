/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.roles

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<RoleDto>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.roles
        crud = Roles

        + RoleDto::id
        + RoleDto::name
        + RoleDto::description

        + actions()
    }

}