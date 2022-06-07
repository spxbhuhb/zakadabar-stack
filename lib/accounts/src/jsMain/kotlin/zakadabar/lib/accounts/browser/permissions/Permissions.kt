/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.permissions

import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.lib.accounts.data.PermissionBo

class Permissions : ZkCrudTarget<PermissionBo>() {

    init {
        companion = PermissionBo.Companion
        boClass = PermissionBo::class
        tableClass = Table::class
        editorClass = Form::class
    }

}