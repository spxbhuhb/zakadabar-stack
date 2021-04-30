/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.flavour

import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.parser.LinkMap

class ZkFlavourDescriptor(
    val context: ZkMarkdownContext
) : GFMFlavourDescriptor() {

    override fun createHtmlGeneratingProviders(linkMap: LinkMap, baseURI: URI?): Map<IElementType, GeneratingProvider> {
        val providers = super.createHtmlGeneratingProviders(linkMap, baseURI).toMutableMap()
        providers[MarkdownTokenTypes.ATX_CONTENT] = HeaderTextProvider(context)
        providers[MarkdownElementTypes.ATX_1] = HeaderProvider(context, 1, "h1")
        providers[MarkdownElementTypes.ATX_2] = HeaderProvider(context, 2, "h2")
        providers[MarkdownElementTypes.ATX_3] = HeaderProvider(context, 3, "h3")
        providers[MarkdownElementTypes.ATX_4] = HeaderProvider(context, 4, "h4")
        providers[MarkdownElementTypes.ATX_5] = HeaderProvider(context, 5, "h5")
        providers[MarkdownElementTypes.ATX_6] = HeaderProvider(context, 7, "h6")
        return providers
    }

}