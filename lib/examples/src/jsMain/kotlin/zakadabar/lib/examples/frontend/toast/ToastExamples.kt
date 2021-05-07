/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.*
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.toast.*
import zakadabar.stack.frontend.util.marginBottom

/**
 * This example shows how to create toasts.
 */
class ToastExamples(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(ZkPageStyles.content) {

            + grid {
                gridTemplateColumns = "repeat(3,max-content)"
                gridGap = theme.layout.spacingStep

                + primaryButton("Primary") { primaryToast { "This is a primary toast!" } }

                + secondaryButton("Secondary") { secondaryToast { "This is a secondary toast!" } }

                + successButton("Success") { successToast { "This is a success toast!" } }

                + warningButton("Warning") { warningToast { "This is a warning toast!" } }

                + dangerButton("Danger") { dangerToast { "This is a danger toast!" } }

                + infoButton("Info") { infoToast { "This is a info toast!" } }

            } marginBottom theme.layout.spacingStep

        }
    }

}