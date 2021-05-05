/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.site.data.ContentQuery
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.util.io

object Welcome : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        io {
            + MarkdownView("/${ContentQuery.dtoNamespace}/Welcome.md") css ZkPageStyles.content
        }
    }

}