/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.form.submit.enter

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.util.default

class SubmitOnEnter : ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo : ExampleBo = default()

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        + bo::stringValue submitOnEnter true
    }

    override fun submit() {
        toastSuccess { "Submit: ${bo.stringValue}" }
    }
}