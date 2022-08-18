/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.dom

import org.w3c.dom.Node
import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBridge

/**
 * The default adapter for W3C DOM nodes used in browsers.
 */
class RuiDOMAdapter : RuiAdapter<Node> {
    override fun createPlaceholder(): RuiBridge<Node> {
        return RuiDOMPlaceholder()
    }
}