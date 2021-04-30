/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.lib.frontend.form

import zakadabar.demo.lib.data.builtin.BuiltinDto
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.synthetic.ZkSyntheticForm
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.util.default

/**
 * This example shows all built in form fields.
 */
object SyntheticForm : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        val dto: BuiltinDto = default { }
        val form = ZkSyntheticForm(dto.schema().toDescriptorDto())
        form.mode = ZkElementMode.Other

        + div(ZkPageStyles.content) {
            + form
        }

    }

}