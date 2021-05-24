/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend

import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.input.ZkTextAreaInput
import zakadabar.stack.frontend.builtin.modal.ZkModalBase
import zakadabar.stack.frontend.util.io

open class ImportDialog : ZkModalBase<String?>() {

    override var titleText: String? = "Import from Source Code"

    override fun buildContent() {
        + "Copy the source code of your BO into the text area, then click on Import"
        + ZkTextAreaInput() build {
            height = 400
            width = "100%"
        }
    }

    override fun buildButtons() {
        + ZkButton("Cancel", onClick = ::onCancel)
        + ZkButton("Import", onClick = ::onOk)
    }

    open fun onOk() = io {
        channel.send(first<ZkTextAreaInput>().value)
    }

    open fun onCancel() = io {
        channel.send(null)
    }
}