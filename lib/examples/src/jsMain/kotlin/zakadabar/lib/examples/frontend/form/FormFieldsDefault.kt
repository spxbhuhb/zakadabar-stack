/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import zakadabar.lib.examples.frontend.crud.BuiltinForm
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.pages.zkPageStyles
import zakadabar.core.frontend.util.default
import zakadabar.core.util.PublicApi

@PublicApi // example code
class FormFieldsDefault : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        val form = BuiltinForm()
        form.bo = default()
        form.mode = ZkElementMode.Update

        + div(zkPageStyles.content) {
            + form
        }
    }

}