/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.util.w3c

import org.w3c.dom.Element

@Suppress("unused") // api
external class ResizeObserver(callback: (Array<ResizeObserverEntry>, ResizeObserver) -> Unit) {
    fun observe(element: Element)
    fun unobserve(element: Element)
    fun disconnect()
}