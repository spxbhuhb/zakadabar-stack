/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.plusAssign

open class ZkIcon(
    val icon: String,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    constructor(source: ZkIconSource, onClick: (() -> Unit)? = null) : this(source.svg(24), onClick)

    override fun onCreate() {
        classList += ZkIconStyles.icon
        element.innerHTML = icon
        if (onClick != null) {
            on("click") { _ -> onClick.invoke() }
            on("mousedown", ::onMouseDown)
        }
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}