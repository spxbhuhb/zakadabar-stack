/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.toast

import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.toast.toast
import zakadabar.stack.frontend.util.marginBottom

/**
 * This example shows how to create checkbox lists.
 */
object Toasts : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        + column(ZkPageStyles.content) {
            + grid {
                gridTemplateColumns = "repeat(4,max-content)"
                gridGap = theme.layout.spacingStep
                + ZkButton(Strings.successToast, onClick = ::onShowSuccess)
                + ZkButton(Strings.infoToast, onClick = ::onShowInfo)
                + ZkButton(Strings.warningToast, onClick = ::onShowWarning)
                + ZkButton(Strings.errorToast, onClick = ::onShowError)
            } marginBottom theme.layout.spacingStep
        }
    }

    private fun onShowSuccess() {
        toast { "This is a success toast!" }
    }

    private fun onShowInfo() {
        toast(info = true) { "This is an info toast!" }
    }

    private fun onShowWarning() {
        toast(warning = true) { "This is a warning toast!" }
    }

    private fun onShowError() {
        toast(error = true) { "This is an error toast!" }
    }

}