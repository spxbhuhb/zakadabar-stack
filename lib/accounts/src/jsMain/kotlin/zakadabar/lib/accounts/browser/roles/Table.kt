/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.roles

import zakadabar.lib.accounts.data.RoleBo
import zakadabar.core.browser.application.target
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localizedStrings

class Table : ZkTable<RoleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = localizedStrings.roles
        crud = target<Roles>()

        + RoleBo::id
        + RoleBo::name
        + RoleBo::description

        + actions()
    }

}