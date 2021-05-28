/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.roles

import zakadabar.lib.accounts.data.RoleBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<RoleBo>() {

    override fun onCreate() {
        build(bo.description, stringStore.role, css = ZkFormStyles.onePanel) {
            + section(stringStore.basics) {
                + bo::id
                + bo::name
                + bo::description
            }
        }
    }
}