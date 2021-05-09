/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.lib.examples.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.util.default

/**
 * This example shows all built in form fields.
 */
class FormBooleanExample(
    element: HTMLElement
) : ZkForm<BuiltinDto>(element) {

    override fun onConfigure() {
        super.onConfigure()
        dto = default { }
        mode = ZkElementMode.Action
        appTitle = false
    }

    override fun onCreate() {
        super.onCreate()

        + section {
            + dto::booleanValue
            + opt(dto::optBooleanValue, Strings.textForFalse, Strings.textForTrue)
        }
    }

}