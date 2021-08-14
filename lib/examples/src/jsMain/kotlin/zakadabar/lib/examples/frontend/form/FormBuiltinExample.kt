/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.frontend.crud.BuiltinForm
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import zakadabar.core.frontend.builtin.toast.toastSuccess
import zakadabar.core.frontend.util.default

class FormBuiltinExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        + div {
            + zkLayoutStyles.p1
            + zkLayoutStyles.fixBorder

            + BuiltinForm().apply {
                bo = default {  }
                mode = ZkElementMode.Other
                setAppTitle = false
                onBack = { toastSuccess { "You've just clicked on \"Back\"." } }
            }
        }
    }

}