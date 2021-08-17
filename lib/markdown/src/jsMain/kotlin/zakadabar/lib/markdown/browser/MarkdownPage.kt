/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.browser

import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.util.io
import zakadabar.lib.markdown.browser.flavour.ZkMarkdownContext

/**
 * Displays a single markdown file.
 */
open class MarkdownPage(
    private val url: String,
    val context: ZkMarkdownContext
) : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        io {
            + MarkdownView(url, context = context)
        }
    }

}