/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import zakadabar.stack.data.builtin.account.RoleBo
import zakadabar.stack.data.builtin.resources.SettingBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<SettingBo>() {

    private val roles by preload {
        try {
            RoleBo.allAsMap()
        } catch (ex: Exception) {
            emptyMap()
        }
    }

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.roles
        crud = Settings

        + SettingBo::id
        + custom {
            label = stringStore.role
            render = { + roles[it.role]?.name }
        }
        + SettingBo::namespace
        + SettingBo::className

        + actions()
    }

}