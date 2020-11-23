/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.simple

import kotlinx.browser.document
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.elements.ZkElement

open class SimpleDateTime(val value: Instant) : ZkElement(
    element = document.createElement("span") as HTMLElement
) {

    override fun init(): ZkElement {
        element.innerText = value.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        return this
    }

}
