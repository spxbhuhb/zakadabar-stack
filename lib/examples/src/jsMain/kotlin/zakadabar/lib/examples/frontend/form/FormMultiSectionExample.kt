/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.resources.strings
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.toast.toastSuccess
import zakadabar.stack.frontend.util.default

class MultiSectionExampleForm : ZkForm<BuiltinBo>() {
    override fun onCreate() {
        super.onCreate()

        + section(strings.basics, "Section summary helps the user to put the data fields into a context.") {
            + bo::id
            + bo::stringValue
        }

        + section("Details", "When fieldGrid = false.", fieldGrid = false) {
            + bo::optStringValue
            + bo::optDoubleValue
        }

        + section("Details 2", "This is the second details section.") {
            + bo::optIntValue
        }

        + buttons()
    }
}


/**
 * This example shows a simple form.
 */
class FormMultiSectionExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        + div {
            + zkLayoutStyles.p1
            + zkLayoutStyles.fixBorder

            + MultiSectionExampleForm().apply {
                bo = default {
                    id = EntityId(123)
                    stringValue = "hello"
                    optStringValue = "hello world"
                    optIntValue = 123
                }
                mode = ZkElementMode.Other
                setAppTitle = false
                onBack = { toastSuccess { "You've just clicked on \"Back\"." } }
            }
        }
    }

}