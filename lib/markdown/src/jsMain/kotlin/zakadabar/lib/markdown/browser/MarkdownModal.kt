/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.browser

import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.modal.ZkModalBase
import zakadabar.core.browser.util.io
import zakadabar.core.resource.css.OverflowY
import zakadabar.core.resource.css.percent
import zakadabar.core.resource.css.px
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.markdown.browser.flavour.ZkMarkdownContext

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