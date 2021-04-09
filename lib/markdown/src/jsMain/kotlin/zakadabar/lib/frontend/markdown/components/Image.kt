/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.components

import org.intellij.markdown.MarkdownElementTypes.LINK_DESTINATION
import org.intellij.markdown.MarkdownElementTypes.LINK_TEXT
import org.intellij.markdown.MarkdownTokenTypes.Companion.HTML_TAG
import org.intellij.markdown.MarkdownTokenTypes.Companion.TEXT
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLLinkElement
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.stack.frontend.util.plusAssign

open class Image(
    view: MarkdownView,
    node: ASTNode
) : MarkdownComponentBase(view, node, "img") {

    open val tagPattern = Regex("<img(\\s+[a-zA-Z]+=\"[a-zA-Z0-9.]*\")+\\s*/>")
    open val attrPattern = Regex("[a-zA-Z]+=\"[a-zA-Z0-9.]*\"")

    override fun onCreate() {
        element as HTMLImageElement
        element.src = destination()

        val linkText = node.find(LINK_TEXT) ?: return

        val htmlTag = linkText.find(HTML_TAG)?.getTextInNode(view.source)
        val text = linkText.find(TEXT)?.getTextInNode(view.source)?.toString() ?: ""

        if (htmlTag != null && tagPattern.matches(htmlTag)) {
            attrPattern.findAll(htmlTag).forEach {
                val (tag, value) = it.value.split("=")
                when (tag.toLowerCase()) {
                    "width" -> element.width = value.trim('"').replace("px", "").toInt()
                    "height" -> element.height = value.trim('"').replace("px", "").toInt()
                    "alt" -> element.alt = value.trim('"')
                }
            }
        } else {
            element.alt = text
        }

    }

    open fun destination(): String {
        return node.find(LINK_DESTINATION)?.find(TEXT)?.getTextInNode(view.source)?.toString() ?: ""
    }

}