/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import org.intellij.markdown.ast.ASTNode
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.lib.frontend.markdown.components.Image
import zakadabar.site.data.ContentQuery

class SiteMarkdownImage(
    view: MarkdownView,
    node: ASTNode
) : Image(view, node) {

    override fun destination() = "/api/${ContentQuery.namespace}/" + super.destination()

}