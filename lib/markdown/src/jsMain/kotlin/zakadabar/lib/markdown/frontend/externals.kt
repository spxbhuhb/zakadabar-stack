/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

@JsModule("highlight.js")
@JsNonModule
external object hljs {
    fun highlightElement(element: dynamic)
}