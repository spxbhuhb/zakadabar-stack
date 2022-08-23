/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.dom

import kotlinx.browser.window
import org.w3c.dom.Node
import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiAdapterRegistry
import zakadabar.rui.runtime.RuiBridge

/**
 * The default adapter for W3C DOM nodes used in browsers.
 */
class RuiDOMAdapter(
    val node : Node = requireNotNull(window.document.body) { "window.document.body is null or undefined" }
) : RuiAdapter<Node> {

    override val rootBridge = RuiDOMPlaceholder().also {
        node.appendChild(it.receiver)
    }

    override fun createPlaceholder(): RuiBridge<Node> {
        return RuiDOMPlaceholder()
    }

    override fun newId(): Int {
        TODO("Not yet implemented")
    }

    companion object {
        init {
            RuiAdapterRegistry.register(RuiDOMAdapterFactory)
        }
    }
}