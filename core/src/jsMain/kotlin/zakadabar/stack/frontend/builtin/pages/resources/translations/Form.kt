/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.translations

import zakadabar.stack.data.builtin.resources.TranslationBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<TranslationBo>() {

    override fun onCreate() {
        build(bo.name, stringStore.setting, css = ZkFormStyles.onePanel) {
            + section(stringStore.basics) {
                + bo::id
                + bo::locale
                + bo::name
                + bo::value
            }
        }
    }
}