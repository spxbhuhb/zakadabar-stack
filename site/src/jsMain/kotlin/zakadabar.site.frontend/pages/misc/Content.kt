/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.io

object Content : ZkPage() {

    fun open(content: ContentEntry) {
        clear()
        open()
        io {
            + MarkdownView("/api/${ContentQuery.namespace}/${content.path}")
        }
    }

}