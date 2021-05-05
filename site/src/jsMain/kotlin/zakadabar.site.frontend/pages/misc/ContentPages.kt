/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.lib.markdown.frontend.MarkdownPages
import zakadabar.stack.frontend.application.ZkApplication

object ContentPages : MarkdownPages() {

    fun open(path: String) {
        ZkApplication.changeNavState("/$viewName/$path")
    }

    override fun url(): String {
        return "/content/$path"
    }
}