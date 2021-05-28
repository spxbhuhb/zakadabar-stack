/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import zakadabar.stack.data.builtin.resources.SettingBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<SettingBo>() {


    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.roles
        crud = Settings

        + SettingBo::id
        + SettingBo::namespace
        + SettingBo::className

        + actions()
    }

}