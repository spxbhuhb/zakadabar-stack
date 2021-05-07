/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.infoButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.toast.ZkToast
import zakadabar.stack.frontend.util.default

/**
 * This example shows how to create toasts.
 */
class ToastFormExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(ZkPageStyles.content) {
            + infoButton("Form in a Toast") {
                ZkToast(content = InlineForm(), hideAfter = null).run()
            } marginBottom theme.spacingStep
        }
    }

    class InlineForm : ZkForm<BuiltinDto>() {

        override fun onConfigure() {
            dto = default { }
            mode = ZkElementMode.Read
            appTitle = false
        }

        override fun onCreate() {
            super.onCreate()

            element.style.margin = "20px"

            + section("This is a form in a toast!") {
                + dto::id
                + dto::doubleValue
                + dto::instantValue
                + dto::optInstantValue
                + dto::stringValue
            }
        }
    }
}