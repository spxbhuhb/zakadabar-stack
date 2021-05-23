/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.locales

import zakadabar.stack.data.builtin.resources.LocaleBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<LocaleBo>() {

    override fun onCreate() {
        build(bo.name, stringStore.setting, css = ZkFormStyles.onePanel) {
            + section(stringStore.basics) {
                + bo::id
                + bo::name
                + bo::description
            }
        }
    }
}