/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.locales

import zakadabar.stack.data.builtin.resources.LocaleDto
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<LocaleDto>() {

    override fun onCreate() {
        build(dto.name, stringStore.setting, css = ZkFormStyles.onePanel) {
            + section(stringStore.basics) {
                + dto::id
                + dto::name
                + dto::description
            }
        }
    }
}