/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.dom

import org.w3c.dom.Node
import zakadabar.rui.runtime.RuiBridge

class RuiDOMPlaceholder : RuiBridge<Node> {

    override val receiver = org.w3c.dom.Text()

    override fun remove(child: RuiBridge<Node>) {
        throw IllegalStateException()
    }

    override fun replace(oldChild: RuiBridge<Node>, newChild: RuiBridge<Node>) {
        throw IllegalStateException()
    }

    override fun add(child: RuiBridge<Node>) {
        throw IllegalStateException()
    }

}