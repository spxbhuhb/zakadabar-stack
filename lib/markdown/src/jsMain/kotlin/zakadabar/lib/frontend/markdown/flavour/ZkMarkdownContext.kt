/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown.flavour

class ZkMarkdownContext {

    var nextId = 1

    var headerText: String = ""

    val tableOfContents = mutableListOf<TocEntry>()

    class TocEntry(
        val id: Int,
        val level: Int,
        val text: String
    )
}