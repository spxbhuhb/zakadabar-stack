/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<SettingDto>() {

    override fun onCreate() {
        build(dto.className, strings.setting, css = ZkFormStyles.onePanel) {
            + section(strings.basics) {
                + dto::id
                + select(dto::role) { RoleDto.all().by { it.name } }
                + dto::className
                + dto::value
            }
        }
    }
}