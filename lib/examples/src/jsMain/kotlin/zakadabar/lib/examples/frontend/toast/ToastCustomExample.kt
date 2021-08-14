/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.button.buttonSecondary
import zakadabar.core.frontend.builtin.icon.ZkIcon
import zakadabar.core.frontend.builtin.pages.zkPageStyles
import zakadabar.core.frontend.builtin.toast.ZkToast
import zakadabar.core.frontend.resources.ZkColors
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.resources.ZkIcons
import zakadabar.core.frontend.resources.css.ZkCssStyleSheet
import zakadabar.core.frontend.resources.css.cssStyleSheet
import zakadabar.core.frontend.resources.theme

/**
 * This example shows how to create toasts.
 */
class ToastCustomExample(
    element: HTMLElement
) : ZkElement(element) {

    companion object {
        val customToastStyles by cssStyleSheet(CustomToastStyles())

        class CustomToastStyles : ZkCssStyleSheet() {
            val customInner by cssClass {
                border = "1px solid ${ZkColors.Zakadabar.navCyan}"
                backgroundColor = ZkColors.Zakadabar.navCyan + "20"
            }
            val customIcon by cssClass {
                backgroundColor = ZkColors.Zakadabar.navCyan
                fill = ZkColors.Zakadabar.gray8
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        + row(zkPageStyles.content) {

            + ZkToast(
                "This is custom toast!",
                flavour = ZkFlavour.Custom,
                icon = ZkIcon(ZkIcons.cloudUpload),
                iconClass = customToastStyles.customIcon,
                innerClass = customToastStyles.customInner
            ) marginRight theme.spacingStep

            + buttonSecondary("Open as Toast") {
                ZkToast(
                    "This is custom toast!",
                    flavour = ZkFlavour.Custom,
                    icon = ZkIcon(ZkIcons.cloudUpload),
                    iconClass = customToastStyles.customIcon,
                    innerClass = customToastStyles.customInner
                ).run()
            }

        }
    }

}