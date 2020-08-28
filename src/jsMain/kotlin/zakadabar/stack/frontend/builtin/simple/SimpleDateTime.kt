/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.simple

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.elements.SimpleElement
import kotlin.js.Date

open class SimpleDateTime(value: Long) : SimpleElement(
    element = document.createElement("span") as HTMLElement
) {

    init {
        element.innerText = Date(value * 1000).toLocaleString()
    }

}
