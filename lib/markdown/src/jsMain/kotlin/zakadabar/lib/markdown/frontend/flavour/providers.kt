/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend.flavour

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.*
import org.intellij.markdown.html.HtmlGenerator.Companion.leafText
import org.intellij.markdown.html.entities.EntityConverter
import org.intellij.markdown.parser.LinkMap

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

abstract class LinkGeneratingProvider(
    protected val context: ZkMarkdownContext
) : GeneratingProvider {
    override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
        val info = getRenderInfo(text, node) ?: return fallbackProvider.processNode(visitor, text, node)
        renderLink(visitor, text, node, info)
    }

    open fun renderLink(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode, info: RenderInfo) {
        val url = context.makeUrl(info.destination)
        if (url.startsWith("https://")) {
            visitor.consumeTagOpen(node, "a", "target=\"_blank\"", "href=\"${url}\"", info.title?.let { "title=\"$it\"" })
        } else {
            visitor.consumeTagOpen(node, "a", "href=\"${url}\"", "class=\"zk-local-nav\"", info.title?.let { "title=\"$it\"" })
        }
        labelProvider.processNode(visitor, text, info.label)
        visitor.consumeTagClose("a")
    }

    abstract fun getRenderInfo(text: String, node: ASTNode): RenderInfo?

    data class RenderInfo(val label: ASTNode, val destination: CharSequence, val title: CharSequence?)

    companion object {
        val fallbackProvider = TransparentInlineHolderProvider()

        val labelProvider = TransparentInlineHolderProvider(1, - 1)
    }
}

class InlineLinkGeneratingProvider(context: ZkMarkdownContext) : LinkGeneratingProvider(context) {
    override fun getRenderInfo(text: String, node: ASTNode): RenderInfo? {

        val label = node.findChildOfType(MarkdownElementTypes.LINK_TEXT) ?: return null

        return RenderInfo(
            label,
            node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(text)?.let {
                LinkMap.normalizeDestination(it, true)
            } ?: "",
            node.findChildOfType(MarkdownElementTypes.LINK_TITLE)?.getTextInNode(text)?.let {
                LinkMap.normalizeTitle(it)
            }
        )
    }
}

class ReferenceLinksGeneratingProvider(context: ZkMarkdownContext, private val linkMap: LinkMap) : LinkGeneratingProvider(context) {
    override fun getRenderInfo(text: String, node: ASTNode): RenderInfo? {
        val label = node.children.firstOrNull({ it.type == MarkdownElementTypes.LINK_LABEL })
            ?: return null
        val linkInfo = linkMap.getLinkInfo(label.getTextInNode(text))
            ?: return null
        val linkTextNode = node.children.firstOrNull({ it.type == MarkdownElementTypes.LINK_TEXT })

        return LinkGeneratingProvider.RenderInfo(
            linkTextNode ?: label,
            EntityConverter.replaceEntities(linkInfo.destination, true, true),
            linkInfo.title?.let { EntityConverter.replaceEntities(it, true, true) }
        )
    }
}

internal class ImageGeneratingProvider(context: ZkMarkdownContext, linkMap: LinkMap) : LinkGeneratingProvider(context) {
    val referenceLinkProvider = ReferenceLinksGeneratingProvider(context, linkMap)
    val inlineLinkProvider = InlineLinkGeneratingProvider(context)

    override fun getRenderInfo(text: String, node: ASTNode): RenderInfo? {
        node.findChildOfType(MarkdownElementTypes.INLINE_LINK)?.let { linkNode ->
            return inlineLinkProvider.getRenderInfo(text, linkNode)
        }
        (node.findChildOfType(MarkdownElementTypes.FULL_REFERENCE_LINK)
            ?: node.findChildOfType(MarkdownElementTypes.SHORT_REFERENCE_LINK))
            ?.let { linkNode ->
                return referenceLinkProvider.getRenderInfo(text, linkNode)
            }
        return null
    }

    override fun renderLink(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode, info: LinkGeneratingProvider.RenderInfo) {
        visitor.consumeTagOpen(
            node, "img",
            "src=\"${context.makeUrl(info.destination)}\"",
            "alt=\"${getPlainTextFrom(info.label, text)}\"",
            info.title?.let { "title=\"$it\"" },
            autoClose = true
        )
    }

    private fun getPlainTextFrom(node: ASTNode, text: String): CharSequence {
        return REGEX.replace(node.getTextInNode(text), "")
    }

    companion object {
        val REGEX = Regex("[^a-zA-Z0-9 ]")
    }
}