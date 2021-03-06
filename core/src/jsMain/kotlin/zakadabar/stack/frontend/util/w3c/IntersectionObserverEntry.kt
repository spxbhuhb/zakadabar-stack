/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util.w3c

import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.Element

@Suppress("unused") // api
external class IntersectionObserverEntry {
    val boundingClientRect: DOMRectReadOnly
    val intersectionRatio: Double
    val intersectionRect: DOMRectReadOnly
    val isIntersecting: Boolean
    val rootBounds: DOMRectReadOnly
    val target: Element
    // val time : DOMHighResTimeStamp
}