/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.flavour

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.html.HtmlGenerator.Companion.leafText
import org.intellij.markdown.html.OpenCloseGeneratingProvider
import org.intellij.markdown.html.TrimmingInlineHolderProvider

class HeaderProvider(
    private val context: ZkMarkdownContext,
    private val level: Int,
    private val tagName: String
) : OpenCloseGeneratingProvider() {

    override fun openTag(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
        visitor.consumeTagOpen(node, tagName, "data-toc-id=\"${context.viewId}-${context.nextTocId}\"")
    }

    override fun closeTag(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
        context.tableOfContents += ZkMarkdownContext.TocEntry("${context.viewId}-${context.nextTocId}", level, context.headerText)
        context.nextTocId ++
        visitor.consumeTagClose(tagName)
    }

}

class HeaderTextProvider(
    private val context: ZkMarkdownContext
) : TrimmingInlineHolderProvider() {
    override fun openTag(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
        super.openTag(visitor, text, node)

        var s = ""
        for (child in childrenToRender(node)) {
            if (child is LeafASTNode) {
                s += leafText(text, child)
            }
        }

        context.headerText = s
    }
}