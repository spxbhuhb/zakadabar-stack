/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import zakadabar.lib.markdown.frontend.flavour.ZkFlavourDescriptor
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.PublicApi

open class MarkdownView(
    private val url: String? = null,
    val content: String? = null,
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
            source = content ?: window.fetch(url).await().text().await()
            parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(source)

            val html = HtmlGenerator(source, parsedTree, flavour).generateHtml()

            + grid {
                gridTemplateColumns = "1fr max-content"

                + div(markdownStyles.content) {
                    buildElement.innerHTML = html
                }

                + TableOfContents(context, element.parentElement as HTMLElement) // TODO think about using element.parentElement in MarkdownView
            }

            syntaxHighLight()
            // println(dump("", parsedTree))
        }
    }

    private fun syntaxHighLight() {
        window.requestAnimationFrame {
            element.querySelectorAll("pre").asList().forEach {
                hljs.highlightElement(it.firstChild)
                CodeCopy(it.firstChild as HTMLElement).onCreate()
            }
        }
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