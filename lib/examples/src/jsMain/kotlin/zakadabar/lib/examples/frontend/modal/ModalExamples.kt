/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.modal

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.resources.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.builtin.modal.ZkMessageDialog
import zakadabar.stack.frontend.builtin.note.ZkNote
import zakadabar.stack.frontend.builtin.note.noteSecondary
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom

/**
 * This example shows how to create checkbox lists.
 */
class ModalExamples(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {
            + grid {
                gridTemplateColumns = "repeat(3,max-content)"
                gridGap = theme.spacingStep
                + ZkButton(strings.confirmDialog, onClick = ::onShowConfirm)
                + ZkButton(strings.messageDialog, onClick = ::onShowMessage)
                + ZkButton(strings.exampleDialog, onClick = ::onShowExample)
            } marginBottom theme.spacingStep

            + noteSecondary("Output", "")
        }
    }

    private fun onShowConfirm() {
        io {
            first<ZkNote>().text = ""
            val value = ZkConfirmDialog("Dialog Title", "Message to tell the user what to confirm").run()
            first<ZkNote>().text = "selected option: $value"
        }
    }

    private fun onShowMessage() {
        io {
            first<ZkNote>().text = ""
            ZkMessageDialog("Dialog Title", "Message for the user.").run()
        }
    }


    private fun onShowExample() {
        io {
            first<ZkNote>().text = ""
            val value = MyMessageDialog().run()
            first<ZkNote>().text = value
        }
    }


}