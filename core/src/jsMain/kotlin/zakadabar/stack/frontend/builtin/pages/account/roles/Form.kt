/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.roles

import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<RoleDto>() {

    override fun onCreate() {
        build(dto.description, stringStore.role, css = ZkFormStyles.onePanel) {
            + section(stringStore.basics) {
                + dto::id
                + dto::name
                + dto::description
            }
        }
    }
}