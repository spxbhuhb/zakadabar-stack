/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.core.frontend.builtin.pages.ZkPage
import zakadabar.core.frontend.util.io

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