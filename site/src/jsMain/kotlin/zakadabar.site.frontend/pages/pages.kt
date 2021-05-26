/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import zakadabar.lib.bender.ClassGenerator
import zakadabar.lib.bender.frontend.Bender
import zakadabar.lib.markdown.frontend.MarkdownPage
import zakadabar.lib.markdown.frontend.MarkdownPathPage
import zakadabar.site.frontend.SiteMarkdownContext
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle

const val contentNamespace = "content"

object Welcome : MarkdownPage(
    "/api/$contentNamespace/welcome/Welcome.md",
    SiteMarkdownContext("/Welcome", "welcome/")
)

object WhatsNew : MarkdownPage(
    "/api/$contentNamespace/changelog/WhatsNew.md",
    SiteMarkdownContext("/WhatsNew", "/")
)

object ShowCase : MarkdownPage(
    "/api/$contentNamespace/welcome/ShowCase.md",
    SiteMarkdownContext("/ShowCase", "welcome/")
)

object Roadmap : MarkdownPage(
    "/api/$contentNamespace/Roadmap.md",
    SiteMarkdownContext("/Roadmap", "/")
)

object GetStarted : MarkdownPage(
    "/api/$contentNamespace/GetStarted.md",
    SiteMarkdownContext("/GetStarted", "/")
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

object ServicesAndSupport : MarkdownPage(
    "/api/$contentNamespace/misc/ServicesAndSupport.md",
    SiteMarkdownContext("ServicesAndSupport", "misc/")
)

object Experimental : MarkdownPage(
    "/api/$contentNamespace/internals/Experimental.md",
    SiteMarkdownContext("Experimental", "internals/")
)

object ProjectStatus : MarkdownPage(
    "/api/$contentNamespace/misc/ProjectStatus.md",
    SiteMarkdownContext("ProjectStatus", "misc/")
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