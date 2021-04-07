/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.components

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import zakadabar.lib.frontend.markdown.MarkdownView

class Text(
    view: MarkdownView,
    node: ASTNode
) : MarkdownComponentBase(view, node, "span") {

    override fun onCreate() {
        super.onCreate()
        + node.getTextInNode(view.source).toString()
    }

}