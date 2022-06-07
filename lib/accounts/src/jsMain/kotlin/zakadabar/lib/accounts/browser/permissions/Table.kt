/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.permissions

import zakadabar.core.browser.application.target
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.accounts.data.PermissionBo

class Table : ZkTable<PermissionBo>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = localizedStrings.roles
        crud = target<Permissions>()

        + PermissionBo::id
        + PermissionBo::name
        + PermissionBo::description

        + actions()
    }

}