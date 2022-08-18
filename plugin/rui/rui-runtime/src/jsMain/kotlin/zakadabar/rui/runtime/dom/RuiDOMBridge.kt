/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.dom

import org.w3c.dom.Node
import zakadabar.rui.runtime.RuiBridge

/**
 * Base bridge class for W3C DOM Nodes. Web browser components such as
 * H1, Span etc. are descendants of this class.
 */
interface RuiDOMBridge : RuiBridge<Node> {

    override fun remove(child: RuiBridge<Node>) {
        receiver.removeChild(child.receiver)
    }

    override fun replace(oldChild: RuiBridge<Node>, newChild: RuiBridge<Node>) {
        receiver.replaceChild(oldChild.receiver, newChild.receiver)
    }

    override fun add(child: RuiBridge<Node>) {
        receiver.appendChild(child.receiver)
    }

}