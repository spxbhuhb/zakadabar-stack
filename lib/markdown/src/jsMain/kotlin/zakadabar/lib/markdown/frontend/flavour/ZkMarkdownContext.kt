/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend.flavour

class ZkMarkdownContext(
    var viewId: String
) {

    var nextTocId = 1

    var headerText: String = ""

    val tableOfContents = mutableListOf<TocEntry>()

    class TocEntry(
        val tocId: String,
        val level: Int,
        val text: String
    )
}