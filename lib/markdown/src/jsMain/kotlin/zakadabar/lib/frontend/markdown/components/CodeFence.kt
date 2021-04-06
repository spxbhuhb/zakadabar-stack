/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.components

import kotlinx.browser.document
import org.intellij.markdown.MarkdownTokenTypes.Companion.CODE_FENCE_CONTENT
import org.intellij.markdown.MarkdownTokenTypes.Companion.FENCE_LANG
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.w3c.dom.HTMLElement
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.lib.frontend.markdown.hljs
import zakadabar.stack.frontend.util.plusAssign

class CodeFence(
    view: MarkdownView,
    node: ASTNode
) : MarkdownComponentBase(view, node, "pre") {

    override fun onCreate() {
        classList += view.styles.codeFence

        val lang = node.find(FENCE_LANG)?.getTextInNode(view.source)?.toString() ?: ""
        val content = collectContent()

        val code = document.createElement("code") as HTMLElement
        code.className = lang
        code.innerText = content

        + code

        hljs.highlightElement(code)
    }

    private fun collectContent(): String {
        val lines = mutableListOf<String>()

        node.children.forEach {
            when (it.type) {
                CODE_FENCE_CONTENT -> lines += it.getTextInNode(view.source).toString()
            }
        }

        return lines.joinToString("\n")
    }

}