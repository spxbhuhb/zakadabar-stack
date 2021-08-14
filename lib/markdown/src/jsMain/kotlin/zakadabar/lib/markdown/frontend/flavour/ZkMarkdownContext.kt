/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend.flavour

import org.intellij.markdown.html.URI
import org.intellij.markdown.html.resolveToStringSafe
import org.w3c.dom.HTMLElement
import zakadabar.core.frontend.builtin.ZkElement

/**
 * Context for the markdown rendering process. Main purposes of this class is
 * to collect table of contents information and to provide URL resolution.
 *
 * @param  baseURI  base url for url resolution
 * @param  toc      When true (default), table of contents is added. When false, not.
 * @param  hashes   When true (default), DOM node ids for hashtag navigation is added. When false, not.
 */
open class ZkMarkdownContext(
    baseURI: String? = null,
    val toc : Boolean = true,
    val hashes : Boolean = true
) {
    lateinit var viewId: String

    var nextTocId = 1

    var headerText: String = ""

    val tableOfContents = mutableListOf<TocEntry>()

    val baseURI = baseURI?.let { URI(it) }

    class TocEntry(
        val tocId: String,
        val level: Int,
        val text: String
    )

    /**
     * Called by HTML generators to make an URL.
     */
    open fun makeUrl(destination: CharSequence): CharSequence {
        if (destination.startsWith('#')) return destination
        return baseURI?.resolveToStringSafe(destination.toString()) ?: destination
    }

    /**
     * Called at the end of markdown processing to enrich the markdown with
     * ZkElement instances.
     *
     * @param  htmlElement  The element in the HTML DOM with "data-zk-md-enrich" attribute present.
     */
    open fun enrich(htmlElement: HTMLElement): ZkElement? = null

    fun resolveToStringSafe(uri: String, destination: String) = URI(uri).resolveToStringSafe(destination)

    /**
     * Clears the context. It a safety feature for the case a context is used more
     * than once.
     */
    fun clear() {
        nextTocId = 1
        headerText = ""
        tableOfContents.clear()
    }
}