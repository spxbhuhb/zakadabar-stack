/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonSecondary
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.toast.ZkToast
import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.theme

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
                fill = ZkColors.Zakadabar.gray1
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