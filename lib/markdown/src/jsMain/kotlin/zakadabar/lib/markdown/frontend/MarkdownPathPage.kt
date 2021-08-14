/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.core.frontend.builtin.pages.ZkPathPage
import zakadabar.core.frontend.util.io

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