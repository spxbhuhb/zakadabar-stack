/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.application.application
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.page.ZkPathPage
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.toast.toastWarning
import zakadabar.core.browser.util.io
import zakadabar.core.resource.ZkFlavour
import zakadabar.lib.bender.ClassGenerator
import zakadabar.lib.bender.frontend.Bender
import zakadabar.lib.lucene.data.LuceneQuery
import zakadabar.lib.lucene.data.LuceneQueryResult
import zakadabar.lib.markdown.browser.MarkdownPage
import zakadabar.lib.markdown.browser.MarkdownPathPage
import zakadabar.lib.markdown.browser.MarkdownView
import zakadabar.site.frontend.PrintLayout
import zakadabar.site.frontend.SiteMarkdownContext
import zakadabar.site.frontend.contentNamespace
import zakadabar.site.resources.strings

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

object Search : ZkPage(css = zkPageStyles.scrollable) {

    override val factory = false

    val results = ZkElement()

    class Form : ZkForm<LuceneQuery>() {

        override val addLabel = false

        override fun onCreate() {
            super.onCreate()

            build {
                + bo::query submitOnEnter true
            }
        }

        override fun onResume() {
            super.onResume()
            bo::query.find().focusValue()
        }

        override fun submit() {
            if (bo.query.isEmpty()) {
                toastWarning { strings.searchTermNeeded }
                return
            }

            io {
                results.clear()

                val context = SiteMarkdownContext("","")

                val cookbook = mutableListOf<LuceneQueryResult>()
                val guides = mutableListOf<LuceneQueryResult>()
                val changelog = mutableListOf<LuceneQueryResult>()

                bo.execute().forEach {
                    when {
                        it.path.contains("TOC.md") -> Unit
                        it.path.startsWith("cookbook") -> cookbook += it
                        it.path.startsWith("guides") -> guides += it
                        it.path.startsWith("changelog") -> changelog += it
                    }
                }

                fun ZkElement.addResult(result : LuceneQueryResult) {
                    + div {
                            + ZkButton(
                                text = result.title.trim('#').trim(' '),
                                url = context.makeUrl("/doc/" + result.path),
                                flavour = ZkFlavour.Info,
                                fill = false,
                                border = false
                            )
                        }
                }

                cookbook.sortBy { it.title }
                guides.sortBy { it.title }
                changelog.sortBy { it.title }

                results.apply {
                    + h3 { + strings.recipies }
                    cookbook.forEach { this.addResult(it)}
                    + h3 { + strings.guides }
                    guides.forEach { this.addResult(it)}
                    + h3 { + strings.changelog }
                    changelog.forEach { this.addResult(it)}
                }

            }
        }
    }

    override fun onCreate() {
        + div(zkLayoutStyles.p1) {

            + h2 { + strings.search }

            ! strings.searchInfo

            + Form().apply {
                bo = LuceneQuery()
                mode = ZkElementMode.Query
            }


            + h2 { + strings.results }

            + results
        }
    }
}