/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBridge
import zakadabar.rui.runtime.RuiFragment

open class RuiTestEmptyFragment(
    override val ruiAdapter: RuiAdapter<TestNode>,
    override val ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit = {  }
) : RuiFragment<TestNode>, RuiBridge<TestNode> {

    override val receiver = TestNode()

    override fun remove(child: RuiBridge<TestNode>) {
        throw IllegalStateException("There should be no children for a empty fragment.")
    }

    override fun replace(oldChild: RuiBridge<TestNode>, newChild: RuiBridge<TestNode>) {
        throw IllegalStateException("There should be no children for a empty fragment.")
    }

    override fun add(child: RuiBridge<TestNode>) {
        throw IllegalStateException("There should be no children for a empty fragment.")
    }

    override fun ruiMount(bridge: RuiBridge<TestNode>) {
        bridge.add(this)
    }

    override fun ruiCreate() {

    }

    override fun ruiPatch() {

    }

    override fun ruiUnmount(bridge : RuiBridge<TestNode>) {

    }

    override fun ruiDispose() {

    }

}