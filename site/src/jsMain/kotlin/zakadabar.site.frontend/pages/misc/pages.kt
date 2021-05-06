/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.lib.markdown.frontend.MarkdownPage
import zakadabar.lib.markdown.frontend.MarkdownPathPage
import zakadabar.site.data.ContentQuery
import zakadabar.stack.frontend.application.ZkApplication

object Welcome : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/Welcome.md",
    ContentContext("/Welcome", "/")
)

object GetStarted : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/GetStarted.md",
    ContentContext("/GetStarted", "/")
)

object GetHelp : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/GetHelp.md",
    ContentContext("/GetHelp", "/")
)

object FAQ : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/faq/FAQ.md",
    ContentContext("FAQ", "faq/")
)

object ContentPages : MarkdownPathPage() {

    fun open(path: String) {
        ZkApplication.changeNavState("/$viewName/$path")
    }

    override fun url(): String {
        return "/content/$path"
    }

    override fun context() = ContentContext(viewName, path)

}