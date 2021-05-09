/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.buttonSecondary
import zakadabar.stack.frontend.builtin.input.ZkTextInput
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.toast.ZkToast
import zakadabar.stack.frontend.builtin.toast.toastDanger
import zakadabar.stack.frontend.builtin.toast.toastInfo
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.util.marginBottom

/**
 * This example shows how to change auto-hide time for a toast.
 */
class ToastAutoHideExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(ZkPageStyles.content) {

            + grid {
                gridTemplateColumns = "repeat(2,max-content)"
                gridGap = theme.spacingStep

                + ZkTextInput(
                    enter = true,
                    value = ZkToast.autoHideDefaults[ZkFlavour.Info].toString()
                ) { runToast() }

                + buttonSecondary("Try It") { runToast() }

            } marginBottom theme.spacingStep

        }
    }

    fun runToast() {
        val value = findFirst<ZkTextInput>().value.toLongOrNull()

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