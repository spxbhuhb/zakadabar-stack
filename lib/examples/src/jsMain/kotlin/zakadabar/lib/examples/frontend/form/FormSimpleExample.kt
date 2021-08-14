/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.lib.examples.resources.strings
import zakadabar.core.data.entity.EntityId
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import zakadabar.core.frontend.builtin.toast.toastSuccess
import zakadabar.core.frontend.util.default
import zakadabar.core.resource.localized

class SimpleExampleForm : ZkForm<SimpleExampleBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<SimpleExampleForm>()) {
            + section(strings.basics) {
                + bo::id
                + bo::name
            }
        }
    }
}


/**
 * This example shows a simple form.
 */
class FormSimpleExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        + div {
            + zkLayoutStyles.p1
            + zkLayoutStyles.fixBorder

            + SimpleExampleForm().apply {
                bo = default {
                    id = EntityId(123)
                    name = "hello world"
                }
                mode = ZkElementMode.Other
                setAppTitle = false
                onBack = { toastSuccess { "You've just clicked on \"Back\"." } }
            }
        }
    }

}