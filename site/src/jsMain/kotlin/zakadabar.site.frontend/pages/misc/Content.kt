/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import kotlinx.serialization.Serializable
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.site.data.ContentQuery
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.pages.ZkArgPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.util.io

object Content : ZkArgPage<Content.Args>(
    Args.serializer()
) {

    @Serializable
    class Args(
        val name: String,
        val path: String
    )

    override fun onCreate() {
        super.onCreate()
        className = ZkPageStyles.fixed
        style {
            overflowY = "auto"
        }
    }

    override fun onResume() {
        clear()

        val params = args ?: return

        ZkApplication.title = ZkAppTitle(params.name)

        io {
            + MarkdownView("/${ContentQuery.dtoNamespace}/${params.path}")
        }
    }

}