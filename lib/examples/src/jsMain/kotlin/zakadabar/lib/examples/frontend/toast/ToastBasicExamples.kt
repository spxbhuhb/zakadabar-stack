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
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.util.marginBottom

/**
 * This example shows how to create toasts.
 */
class ToastBasicExamples(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(ZkPageStyles.content) {

            + grid {
                gridTemplateColumns = "repeat(2,max-content)"
                gridGap = theme.spacingStep

                + ZkToast("This is a primary toast!", flavour = ZkFlavour.Primary)

                + primaryButton("Open as Toast") { primaryToast { "This is a primary toast!" } }

                + ZkToast("This is a secondary toast!", flavour = ZkFlavour.Secondary)

                + secondaryButton("Open as Toast") { secondaryToast { "This is a secondary toast!" } }

                + ZkToast("This is a success toast!", flavour = ZkFlavour.Success)

                + successButton("Open as Toast") { successToast { "This is a success toast!" } }

                + ZkToast("This is a warning toast!", flavour = ZkFlavour.Warning)

                + warningButton("Open as Toast") { warningToast { "This is a warning toast!" } }

                + ZkToast("This is a danger toast!", flavour = ZkFlavour.Danger)

                + dangerButton("Open as Toast") { dangerToast { "This is a danger toast!" } }

                + ZkToast("This is a info toast!", flavour = ZkFlavour.Info)

                + infoButton("Open as Toast") { infoToast { "This is a info toast!" } }

            } marginBottom theme.spacingStep

        }
    }

}