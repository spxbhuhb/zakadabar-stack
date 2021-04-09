/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes.ATX_1
import org.intellij.markdown.MarkdownElementTypes.ATX_2
import org.intellij.markdown.MarkdownElementTypes.ATX_3
import org.intellij.markdown.MarkdownElementTypes.ATX_4
import org.intellij.markdown.MarkdownElementTypes.ATX_5
import org.intellij.markdown.MarkdownElementTypes.ATX_6
import org.intellij.markdown.MarkdownElementTypes.CODE_FENCE
import org.intellij.markdown.MarkdownElementTypes.CODE_SPAN
import org.intellij.markdown.MarkdownElementTypes.EMPH
import org.intellij.markdown.MarkdownElementTypes.IMAGE
import org.intellij.markdown.MarkdownElementTypes.INLINE_LINK
import org.intellij.markdown.MarkdownElementTypes.LINK_DEFINITION
import org.intellij.markdown.MarkdownElementTypes.LINK_TEXT
import org.intellij.markdown.MarkdownElementTypes.LIST_ITEM
import org.intellij.markdown.MarkdownElementTypes.MARKDOWN_FILE
import org.intellij.markdown.MarkdownElementTypes.ORDERED_LIST
import org.intellij.markdown.MarkdownElementTypes.PARAGRAPH
import org.intellij.markdown.MarkdownElementTypes.STRONG
import org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST
import org.intellij.markdown.MarkdownTokenTypes.Companion.ATX_CONTENT
import org.intellij.markdown.MarkdownTokenTypes.Companion.ATX_HEADER
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.MarkdownTokenTypes.Companion.LIST_NUMBER
import org.intellij.markdown.MarkdownTokenTypes.Companion.TEXT
import org.intellij.markdown.MarkdownTokenTypes.Companion.WHITE_SPACE
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import zakadabar.lib.frontend.markdown.components.*
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.PublicApi

open class MarkdownView(
    val url: String? = null,
    var content: String? = null,
    val styles: MarkdownStyles = defaultStyles,
    val lib: MutableMap<IElementType, MarkdownView.(node: ASTNode) -> ZkElement?> = MarkdownView.lib
) : ZkElement() {

    lateinit var source: String
    lateinit var parsedTree: ASTNode

    override fun onCreate() {
        super.onCreate()
        val flavour = CommonMarkFlavourDescriptor()

        io {
            source = content ?: window.fetch(url).await().text().await()
            parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(source)
            + MarkdownComponentBase(this, parsedTree)
            println(dump("", parsedTree))
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

    companion object {
        val defaultStyles = MarkdownStyles()

        val lib = mutableMapOf<IElementType, MarkdownView.(node: ASTNode) -> ZkElement?>(
            ATX_1 to { Atx1(this, it) },
            ATX_2 to { Atx2(this, it) },
            ATX_3 to { Atx3(this, it) },
            ATX_4 to { Atx4(this, it) },
            ATX_5 to { Atx5(this, it) },
            ATX_6 to { Atx6(this, it) },
            ATX_CONTENT to { MarkdownComponentBase(this, it) },
            ATX_HEADER to { null },
            CODE_FENCE to { CodeFence(this, it) },
            CODE_SPAN to { CodeSpan(this, it) },
            EMPH to { null },
            EOL to { Eol(this, it) },
            IMAGE to { Image(this, it) },
            INLINE_LINK to { InlineLink(this, it) },
            LINK_TEXT to { LinkText(this, it) },
            LINK_DEFINITION to { MarkdownComponentBase(this, it) },
            LIST_ITEM to { ListItem(this, it) },
            LIST_NUMBER to { null },
            MARKDOWN_FILE to { MarkdownComponentBase(this, it) },
            ORDERED_LIST to { OrderedList(this, it) },
            PARAGRAPH to { Paragraph(this, it) },
            STRONG to { Strong(this, it) },
            TEXT to { Text(this, it) },
            UNORDERED_LIST to { UnorderedList(this, it) },
            WHITE_SPACE to { WhiteSpace(this, it) }
        )
    }


}