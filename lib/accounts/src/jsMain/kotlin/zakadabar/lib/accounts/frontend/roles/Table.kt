/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.roles

import zakadabar.lib.accounts.data.RoleBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<RoleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.roles
        crud = target<Roles>()

        + RoleBo::id
        + RoleBo::name
        + RoleBo::description

        + actions()
    }

}