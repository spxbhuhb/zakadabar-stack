/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.browser

import zakadabar.core.browser.page.ZkPathPage
import zakadabar.core.browser.util.io
import zakadabar.lib.markdown.browser.flavour.ZkMarkdownContext

/**
 * Displays markdown files of a file hierarchy, one by one.
 * [onResume] loads the markdown file specified by the current
 * URL.
 */
abstract class MarkdownPathPage : ZkPathPage() {

    override fun onResume() {
        clear()
        io {
            + MarkdownView(url(), context = context())
        }
    }

    abstract fun url(): String

    abstract fun context(): ZkMarkdownContext

}