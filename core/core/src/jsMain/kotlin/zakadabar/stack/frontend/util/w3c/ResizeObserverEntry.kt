/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util.w3c

import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.Element

@Suppress("unused") // api
external class ResizeObserverEntry {
    val contentRect: DOMRectReadOnly
    val target: Element
}