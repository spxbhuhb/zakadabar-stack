/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend.flavour

import org.intellij.markdown.html.URI
import org.intellij.markdown.html.resolveToStringSafe

/**
 * Context for the markdown rendering process. Main purposes of this class is
 * to collect table of contents information and to provide URL resolution.
 */
open class ZkMarkdownContext(
    baseURI: String? = null
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