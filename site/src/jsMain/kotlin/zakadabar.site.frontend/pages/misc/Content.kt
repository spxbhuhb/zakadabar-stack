/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.pages.ZkArgPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkPageTitle
import zakadabar.stack.frontend.util.io

object Content : ZkArgPage<ContentEntry>(
    ContentEntry.serializer()
) {

    override fun onResume() {
        clear()
        val contentEntry = args ?: return

        ZkApplication.title = ZkPageTitle(contentEntry.name)

        io {
            + MarkdownView("/api/${ContentQuery.namespace}/${contentEntry.path}") css ZkPageStyles.content
        }
    }

}