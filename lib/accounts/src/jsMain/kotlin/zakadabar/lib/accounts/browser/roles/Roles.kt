/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.roles

import zakadabar.lib.accounts.data.RoleBo
import zakadabar.core.browser.crud.ZkCrudTarget

class Roles : ZkCrudTarget<RoleBo>() {

    init {
        companion = RoleBo.Companion
        boClass = RoleBo::class
        tableClass = Table::class
        editorClass = Form::class
    }

}

