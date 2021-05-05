/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import zakadabar.lib.examples.frontend.crud.BuiltinForm
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.util.default

/**
 * This example shows all built in form fields.
 */
object FormFieldsGenerated : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        val form = BuiltinForm()
        form.dto = default()
        form.mode = ZkElementMode.Update

        + div(ZkPageStyles.content) {
            + form
        }
    }

}