/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.ContentTextBo
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm

class ContentTextForm(
    private val editor : ContentEditorForm
) : ZkForm<ContentTextBo>() {

    override fun onConfigure() {
        goBackAfterCreate = false
    }

    override fun onCreate() {
        super.onCreate()

        + section {
            + bo::id
            + bo::type
            + textarea(bo::value, areaHeight = 400)
        }

        + buttons()

    }

    override fun onSubmitSuccess() {
        if (mode == ZkElementMode.Create) {
            editor.onTextCreate(bo)
        }
    }
}
