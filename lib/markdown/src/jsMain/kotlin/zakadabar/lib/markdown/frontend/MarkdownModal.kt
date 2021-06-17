/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.modal.ZkModalBase
import zakadabar.stack.frontend.util.io
import zakadabar.stack.resources.localizedStrings

class MarkdownModal(
    private val title: String? = null,
    private val url: String? = null,
    val content: String? = null,
    private val context: ZkMarkdownContext = ZkMarkdownContext()
) : ZkModalBase<Boolean>() {

    override fun onCreate() {
        titleText = title
        super.onCreate()
    }

    override fun buildContent() {
        + div {
            style {
                maxHeight = "500px"
                width = "100%"
                overflowY = "auto"
            }
            + MarkdownView(url, content, context)
        }
    }

    override fun buildButtons() {
        + ZkButton(localizedStrings.ok, onClick = ::onOk)
    }

    private fun onOk() = io {
        channel.send(true)
    }

}