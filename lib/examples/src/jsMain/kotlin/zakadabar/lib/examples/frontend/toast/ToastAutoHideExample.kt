/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonSecondary
import zakadabar.core.browser.input.ZkTextInput
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.toast.ZkToast
import zakadabar.core.browser.toast.toastDanger
import zakadabar.core.browser.toast.toastInfo
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.css.px
import zakadabar.core.resource.theme
import zakadabar.core.browser.util.marginBottom

/**
 * This example shows how to change auto-hide time for a toast.
 */
class ToastAutoHideExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {

            + grid {
                gridTemplateColumns = "repeat(2,max-content)"
                gridGap = theme.spacingStep.px

                + ZkTextInput(
                    enter = true,
                    value = ZkToast.autoHideDefaults[ZkFlavour.Info].toString()
                ) { runToast() }

                + buttonSecondary("Try It") { runToast() }

            } marginBottom theme.spacingStep

        }
    }

    fun runToast() {
        val value = first<ZkTextInput>().value.toLongOrNull()

        if (value == null || value < 0) {
            toastDanger { "This is not a valid value!" }
            return
        }

        ZkToast.autoHideDefaults[ZkFlavour.Info] = value

        toastInfo {
            if (value == 0L) {
                "This toast will stay here indefinitely, you can close it manually."
            } else {
                "This toast self-destructs after ${value.toDouble() / 1000} seconds."
            }
        }
    }

}