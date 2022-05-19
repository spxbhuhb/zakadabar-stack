/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.page.ZkPathPage
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.util.io
import zakadabar.lib.bender.ClassGenerator
import zakadabar.lib.bender.frontend.Bender
import zakadabar.lib.markdown.browser.MarkdownPage
import zakadabar.lib.markdown.browser.MarkdownPathPage
import zakadabar.lib.markdown.browser.MarkdownView
import zakadabar.site.frontend.PrintLayout
import zakadabar.site.frontend.SiteMarkdownContext
import zakadabar.site.frontend.contentNamespace

object Welcome : MarkdownPage(
    "/api/$contentNamespace/welcome/Welcome.md",
    SiteMarkdownContext("/Welcome", "welcome/")
)

object GetStarted : MarkdownPage(
    "/api/$contentNamespace/GetStarted.md",
    SiteMarkdownContext("/GetStarted", "/")
)

object ShowCase : MarkdownPage(
    "/api/$contentNamespace/welcome/ShowCase.md",
    SiteMarkdownContext("/ShowCase", "welcome/")
)

object GetHelp : MarkdownPage(
    "/api/$contentNamespace/help/GetHelp.md",
    SiteMarkdownContext("/GetHelp", "help/")
)

object LegalNotices : MarkdownPage(
    "/api/$contentNamespace/misc/LegalNotices.md",
    SiteMarkdownContext("/LegalNotices", "misc/")
)

object Credits : MarkdownPage(
    "/api/$contentNamespace/misc/Credits.md",
    SiteMarkdownContext("/Credits", "misc/")
)

object DocumentationIntro : MarkdownPage(
    "/api/$contentNamespace/help/Documentation.md",
    SiteMarkdownContext("/DocumentationIntro", "help/")
)

object FAQ : MarkdownPage(
    "/api/$contentNamespace/help/FAQ.md",
    SiteMarkdownContext("FAQ", "help/")
)

object Versioning : MarkdownPage(
    "/api/$contentNamespace/misc/Versioning.md",
    SiteMarkdownContext("Versioning", "misc/")
)

object BuildAndRelease : MarkdownPage(
    "/api/$contentNamespace/internals/BuildAndRelease.md",
    SiteMarkdownContext("BuildAndRelease", "internals/")
)

object BenderPage : ZkPage() {
    override fun onCreate() {
        + Bender(
            ClassGenerator(),
            "/api/$contentNamespace/template/bender/bo.md",
            markdownContext = { SiteMarkdownContext("Documentation", "guides/", toc = false, hashes = false) }
        )
    }
}

object Documentation : MarkdownPathPage() {

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        application.title = ZkAppTitle("")
    }

    override fun url(): String {
        return "/api/$contentNamespace/$path"
    }

    override fun context() = SiteMarkdownContext(viewName, path)

}

object ChangeLog : MarkdownPathPage() {

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        application.title = ZkAppTitle("")
    }

    override fun url(): String {
        return "/api/$contentNamespace/$path"
    }

    override fun context() = SiteMarkdownContext(viewName, path)

}

object Upgrade : MarkdownPathPage() {

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        application.title = ZkAppTitle("")
    }

    override fun url(): String {
        return "/api/$contentNamespace/$path"
    }

    override fun context() = SiteMarkdownContext(viewName, path)

}

object AllGuides : ZkPathPage(layout = PrintLayout) {

    override fun onCreate() {
        io {
            + MarkdownView(
                "/api/$contentNamespace/guides/All.md",
                context = SiteMarkdownContext(viewName, path, toc = false)
            )
        }
    }

}
