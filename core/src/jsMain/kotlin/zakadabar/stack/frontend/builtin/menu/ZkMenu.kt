/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.elements.ZkClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.plusAssign

open class ZkMenu() : ZkElement() {

    constructor(builder: ZkMenu.() -> Unit) : this() {
        className = MenuStyles.menu
        builder()
    }

    open fun item(text: String, onClick: (() -> Unit)? = null) =
        MenuItem(text, onClick)

    open fun group(text: String, builder: ZkElement.() -> Unit) =
        MenuGroup(text, builder)
}

open class MenuItem(
    private val text: String,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun init() = build {
        className = MenuStyles.item
        + text

        on("click", onClick)
        on("mousedown", ::onMouseDown)

    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}

class MenuGroup(
    private val text: String,
    builder: ZkElement.() -> Unit
) : ZkElement() {

    init {
        + column {
            + div(MenuStyles.groupTitle) {
                + text
                on(buildContext, "click") { _ -> childElements.first().toggle() }
            }
            + zke(MenuStyles.groupContent) {
                classList += ZkClasses.zkClasses.hidden
                this.builder()
            }
        }
    }

}