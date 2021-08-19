/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.popup

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application

open class ZkPopUp() : ZkElement() {

    var shown: Boolean = false

    constructor(builder: ZkPopUp.() -> Unit) : this() {
        this.builder()
    }

    open fun toggle(anchorElement: HTMLElement, minHeight: Int) {
        if (shown) {
            hide()
        } else {
            show(anchorElement, minHeight)
        }
    }

    open fun show(anchorElement: HTMLElement, minHeight: Int) {
        shown = true
        application.popup.appendChild(this.element)
        alignPopup(this.element, anchorElement, minHeight)
    }

    override fun hide(): ZkElement {
        if (shown) {
            shown = false
            element.remove()
        }
        return this
    }

}