/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.synthetic.ZkSyntheticForm
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.util.default

/**
 * This example shows all built in form fields.
 */
object SyntheticForm : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        val dto: zakadabar.lib.examples.data.builtin.BuiltinDto = default { }
        val form = ZkSyntheticForm(dto.schema().toDescriptorDto())
        form.mode = ZkElementMode.Other

        + div(zkPageStyles.content) {
            + form
        }

    }

}