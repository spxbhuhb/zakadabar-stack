/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import zakadabar.lib.frontend.markdown.flavour.ZkFlavourDescriptor
import zakadabar.lib.frontend.markdown.flavour.ZkMarkdownContext
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.PublicApi

open class MarkdownView(
    val url: String? = null,
    var content: String? = null
) : ZkElement() {

    lateinit var source: String
    lateinit var parsedTree: ASTNode

    override fun onCreate() {
        super.onCreate()

        io {
            val context = ZkMarkdownContext()
            val flavour = ZkFlavourDescriptor(context)
            source = content ?: window.fetch(url).await().text().await()
            parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(source)

            val html = HtmlGenerator(source, parsedTree, flavour).generateHtml()

            + grid {
                gridTemplateColumns = "1fr max-content"
                + div {
                    style {
                        overflowX = "auto"
                        padding = "${theme.layout.spacingStep * 2}px"
                        paddingTop = "0px"
                    }
                    buildElement.innerHTML = html
                }
                + TableOfContents(context)
            }
            // println(dump("", parsedTree))
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