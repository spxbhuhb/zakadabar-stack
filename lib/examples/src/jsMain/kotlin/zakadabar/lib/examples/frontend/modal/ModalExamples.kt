/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.modal

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.resources.Strings
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.builtin.modal.ZkMessageDialog
import zakadabar.stack.frontend.builtin.note.ZkNote
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom

/**
 * This example shows how to create checkbox lists.
 */
class ModalExamples(
    element: HTMLElement
) : ZkElement(element) {

    private val output = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + column(ZkPageStyles.content) {
            + grid {
                gridTemplateColumns = "repeat(3,max-content)"
                gridGap = theme.layout.spacingStep
                + ZkButton(Strings.confirmDialog, onClick = ::onShowConfirm)
                + ZkButton(Strings.messageDialog, onClick = ::onShowMessage)
                + ZkButton(Strings.exampleDialog, onClick = ::onShowExample)
            } marginBottom theme.layout.spacingStep

            + ZkNote {
                title = "Output"
                content = output
            }
        }
    }

    private fun onShowConfirm() {
        io {
            output.clear()
            val value = ZkConfirmDialog("Dialog Title", "Message to tell the user what to confirm").run()
            output.build { + "selected option: $value" }
        }
    }

    private fun onShowMessage() {
        io {
            output.clear()
            ZkMessageDialog("Dialog Title", "Message for the user.").run()
        }
    }


    private fun onShowExample() {
        io {
            output.clear()
            val value = MyMessageDialog().run()
            output.build { + value }
        }
    }


}