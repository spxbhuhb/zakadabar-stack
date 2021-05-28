/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import zakadabar.stack.data.builtin.resources.SettingBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<SettingBo>() {

    override fun onCreate() {
        build(bo.className, stringStore.setting, css = ZkFormStyles.onePanel) {
            + section(stringStore.basics) {
                + bo::id
                //+ select(bo::role, options = ::roles)
                + bo::className
            }
        }
    }

}