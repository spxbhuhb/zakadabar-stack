/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown

import zakadabar.lib.frontend.markdown.flavour.ZkMarkdownContext
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.marginBottom

class TableOfContents(
    val context: ZkMarkdownContext
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        style {
            minWidth = "200px"
        }

        + div { + "Table Of Contents" } marginBottom 20

        context.tableOfContents.forEach {
            + div { + it.text }
        }
    }
}