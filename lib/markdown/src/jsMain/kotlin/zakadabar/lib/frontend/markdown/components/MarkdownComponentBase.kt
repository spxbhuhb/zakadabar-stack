/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.components

import kotlinx.browser.document
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.w3c.dom.HTMLElement
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.stack.frontend.builtin.ZkElement

open class MarkdownComponentBase(
    val view: MarkdownView,
    val node: ASTNode,
    htmlType: String = "div"
) : ZkElement(
    document.createElement(htmlType) as HTMLElement
) {

    override fun onCreate() {
        super.onCreate()
        node.children.forEach {
            + (view.lib[it.type]?.invoke(view, it) ?: return@forEach)
        }
    }

    fun ASTNode.find(type: IElementType): ASTNode? {
        children.forEach {
            if (it.type == type) return it
            val descendant = it.find(type)
            if (descendant != null) return descendant
        }
        return null
    }
}