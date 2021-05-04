/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.pages.ZkArgPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.util.io

object Content : ZkArgPage<ContentEntry>(
    ContentEntry.serializer()
) {

    override fun onCreate() {
        super.onCreate()
        className = ZkPageStyles.fixed
        style {
            overflowY = "auto"
        }
    }

    override fun onResume() {
        clear()
        val contentEntry = args ?: return

        ZkApplication.title = ZkAppTitle(contentEntry.name)

        io {
            + MarkdownView("/api/${ContentQuery.dtoNamespace}/${contentEntry.path}")
        }
    }

}