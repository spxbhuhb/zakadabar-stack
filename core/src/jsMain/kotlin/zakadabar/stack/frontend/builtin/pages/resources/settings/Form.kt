/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<SettingDto>() {

    override fun onCreate() {
        build(dto.className, stringStore.setting, css = ZkFormStyles.onePanel) {
            + section(stringStore.basics) {
                + dto::id
                //+ select(dto::role, options = ::roles)
                + dto::className
            }
        }
    }

    suspend fun roles() =
        try {
            RoleDto.all().by { it.name }
        } catch (ex: Exception) {
            emptyList()
        }

}