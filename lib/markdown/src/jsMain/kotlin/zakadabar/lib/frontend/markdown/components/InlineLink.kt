/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.components

import org.intellij.markdown.MarkdownElementTypes.LINK_DESTINATION
import org.intellij.markdown.MarkdownElementTypes.LINK_TEXT
import org.intellij.markdown.MarkdownTokenTypes.Companion.TEXT
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLLinkElement
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.stack.frontend.util.plusAssign

class InlineLink(
    view: MarkdownView,
    node: ASTNode
) : MarkdownComponentBase(view, node, "a") {

    override fun onCreate() {
        classList += view.styles.inlineLink

        + (node.find(LINK_TEXT)?.find(TEXT)?.getTextInNode(view.source)?.toString() ?: "")

        element as HTMLAnchorElement
        element.href = destination()
    }

    private fun destination(): String {
        return node.find(LINK_DESTINATION)?.find(TEXT)?.getTextInNode(view.source)?.toString() ?: ""
    }

}