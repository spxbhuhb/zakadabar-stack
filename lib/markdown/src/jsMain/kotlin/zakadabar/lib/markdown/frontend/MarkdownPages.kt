/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.pages.ZkPathPage
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.util.io

abstract class MarkdownPages : ZkPathPage() {

    override fun onCreate() {
        super.onCreate()
        className = ZkPageStyles.fixed
        style {
            overflowY = "auto"
        }
    }

    override fun onResume() {
        clear()
        setTitle()
        io {
            + MarkdownView(url())
        }
    }

    open fun setTitle() {
        ZkApplication.title = ZkAppTitle(name)
    }

    abstract fun url(): String

}