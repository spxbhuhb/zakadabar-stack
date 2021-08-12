/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.popup


import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement

open class ZkPopUp() : ZkElement() {

    constructor(builder : ZkPopUp.() -> Unit) : this() {
        this.builder()
    }

    open fun toggle(anchorElement: HTMLElement, minHeight: Int) {
        if (isShown()) {
            hide()
        } else {
            show(anchorElement, minHeight)
        }
    }

    open fun show(anchorElement: HTMLElement, minHeight: Int) {
        application.popup.clear()
        application.popup.appendChild(this.element)
        super.show()
        alignPopup(this.element, anchorElement, minHeight)
    }

    override fun hide() : ZkElement {
        application.popup.clear()
        return super.hide()
    }

}