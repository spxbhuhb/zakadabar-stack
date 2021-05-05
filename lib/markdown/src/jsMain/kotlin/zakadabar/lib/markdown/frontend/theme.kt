/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.stack.frontend.resources.ZkTheme

interface MarkdownThemeExt : ZkTheme {
    val markdownTheme: MarkdownTheme
}

class MarkdownTheme(
    val backgroundColor: String? = null,
    val borderColor: String? = null,
    val highlightUrl: String = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/idea.min.css"
)

