/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.browser

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.util.io
import zakadabar.core.util.PublicApi
import zakadabar.lib.markdown.browser.flavour.ZkFlavourDescriptor
import zakadabar.lib.markdown.browser.flavour.ZkMarkdownContext

open class MarkdownView(
    private val sourceUrl: String? = null,
    private val sourceText: String? = null,
    private val context: ZkMarkdownContext = ZkMarkdownContext()
) : ZkElement() {

    lateinit var source: String
    lateinit var parsedTree: ASTNode

    override fun onCreate() {
        super.onCreate()

        io {
            context.clear()
            context.viewId = element.id

            val flavour = ZkFlavourDescriptor(context)
            source = sourceText ?: window.fetch(sourceUrl).await().text().await()
            parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(source)

            val html = HtmlGenerator(source, parsedTree, flavour).generateHtml()

            + div(markdownStyles.container) {

                + div(markdownStyles.content) {
                    buildPoint.innerHTML = html
                }

                if (context.toc) {
                    + TableOfContents(context, element.parentElement as HTMLElement) // TODO think about using element.parentElement in MarkdownView
                }
            }

            window.requestAnimationFrame {
                syntaxHighLight()
                enrich()
                localNavEvents()
                scrollIntoView()
            }

            //println(dump("", parsedTree))
        }
    }

    private fun syntaxHighLight() {
        element.querySelectorAll("pre").asList().forEach {
            hljs.highlightElement(it.firstChild)
            CodeCopy(it.firstChild as HTMLElement).onCreate()
        }
    }

    private fun localNavEvents() {
        element.querySelectorAll(".zk-local-nav").asList().forEach {
            it.addEventListener("click", ::onLocalNav)
        }
    }

    private fun onLocalNav(event: Event) {
        val target = event.target
        if (target !is HTMLAnchorElement) return

        // without this we get the full url with protocol and site and here we don't want that
        val url = js("target.getAttribute('href', 2)") as String

        if (url.startsWith("https://") || url.startsWith("http://") || url.startsWith("mailto:") || url.startsWith("tel:")) return

        event.preventDefault()
        application.changeNavState(url)
    }

    private fun enrich() {
        element.querySelectorAll("[data-zk-enrich]").asList().forEach {
            it as HTMLElement
            context.enrich(it)?.let { child -> addChildSkipDOM(child) }
            it.css(markdownStyles.enrichElement)
        }
    }

    private fun scrollIntoView() {
        if (!context.hashes) return

        val hash = application.routing.navState.hash
        if (hash.isEmpty()) return
        document.getElementById(hash)?.scrollIntoView(
            object {
                val block = "nearest"
                val inline = "nearest"
            }.asDynamic()
        )
    }

    @PublicApi
    open fun dump(indent: String, node: ASTNode): String {
        var s = "$indent${node.type}\n"
        val ni = "$indent    "
        node.children.forEach {
            s += dump(ni, it)
        }
        return s
    }

}