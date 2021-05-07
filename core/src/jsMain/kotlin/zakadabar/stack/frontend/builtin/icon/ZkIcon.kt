/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.resources.ZkIconSource
import zakadabar.stack.frontend.util.plusAssign

open class ZkIcon(
    val icon: String,
    val cssClass: String = ZkIconStyles.icon,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    constructor(source: ZkIconSource, size: Int = 18, onClick: (() -> Unit)? = null) : this(source.svg(size), onClick = onClick)

    constructor(source: ZkIconSource, size: Int = 18, cssClass: String = ZkIconStyles.icon, onClick: (() -> Unit)? = null) : this(source.svg(size), cssClass, onClick = onClick)

    override fun onCreate() {
        classList += cssClass
        element.innerHTML = icon
        if (onClick != null) {
            on("click") { onClick.invoke() }
            on("mousedown", ::onMouseDown)
        }
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}