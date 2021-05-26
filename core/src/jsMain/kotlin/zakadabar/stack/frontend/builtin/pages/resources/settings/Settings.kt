/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import zakadabar.stack.data.builtin.resources.SettingBo
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget

object Settings : ZkCrudTarget<SettingBo>() {

    init {
        companion = SettingBo.Companion
        boClass = SettingBo::class
        tableClass = Table::class
        pageClass = Form::class
    }

}

