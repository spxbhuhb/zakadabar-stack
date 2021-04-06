/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.components

import org.intellij.markdown.ast.ASTNode
import zakadabar.lib.frontend.markdown.MarkdownView

class Atx4(
    view: MarkdownView,
    node: ASTNode
) : MarkdownComponentBase(view, node, "h4")