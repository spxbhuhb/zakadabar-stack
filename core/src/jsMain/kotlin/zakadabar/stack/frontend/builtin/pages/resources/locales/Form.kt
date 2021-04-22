/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.locales

import zakadabar.stack.data.builtin.resources.LocaleDto
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<LocaleDto>() {

    override fun onCreate() {
        build(dto.name, strings.setting, css = ZkFormStyles.onePanel) {
            + section(strings.basics) {
                + dto::id
                + dto::name
                + dto::description
            }
        }
    }
}