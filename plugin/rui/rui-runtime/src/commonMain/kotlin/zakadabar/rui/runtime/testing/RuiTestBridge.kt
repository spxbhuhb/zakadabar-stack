/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiBridge

class RuiTestBridge(
    val id : Int
) : RuiBridge<TestNode> {

    override val receiver = TestNode()

    override fun remove(child: RuiBridge<TestNode>) {
        receiver.removeChild(child.receiver)
    }

    override fun replace(oldChild: RuiBridge<TestNode>, newChild: RuiBridge<TestNode>) {
        receiver.replaceChild(oldChild.receiver, newChild.receiver)
    }

    override fun add(child: RuiBridge<TestNode>) {
        receiver.appendChild(child.receiver)
    }

}