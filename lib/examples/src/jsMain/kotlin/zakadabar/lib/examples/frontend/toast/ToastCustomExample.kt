/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.secondaryButton
import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.builtin.toast.ZkToast
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

/**
 * This example shows how to create toasts.
 */
class ToastCustomExample(
    element: HTMLElement
) : ZkElement(element) {

    companion object {
        val customToastStyles by cssStyleSheet(CustomToastStyles())

        class CustomToastStyles : ZkCssStyleSheet<ZkTheme>() {
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

            + secondaryButton("Open as Toast") {
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