/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

class ZkIconSource(
    val name: String,
    val content: String
) {

    fun svg(size: Int) =
        if (content.startsWith("<svg")) {
            content
        } else {
            """<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 24 24">$content</svg>"""
        }
}