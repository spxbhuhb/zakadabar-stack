/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.modal.ZkModalBase
import zakadabar.core.frontend.resources.css.OverflowY
import zakadabar.core.frontend.resources.css.percent
import zakadabar.core.frontend.resources.css.px
import zakadabar.core.frontend.util.io
import zakadabar.core.resource.localizedStrings

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
                maxHeight = 500.px
                width = 100.percent
                + OverflowY.auto
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