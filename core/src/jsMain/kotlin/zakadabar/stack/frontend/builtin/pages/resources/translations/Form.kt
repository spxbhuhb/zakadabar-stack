/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.translations

import zakadabar.stack.data.builtin.resources.TranslationDto
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<TranslationDto>() {

    override fun onCreate() {
        build(dto.name, strings.setting, css = ZkFormStyles.onePanel) {
            + section(strings.basics) {
                + dto::id
                + dto::locale
                + dto::name
                + dto::value
            }
        }
    }
}