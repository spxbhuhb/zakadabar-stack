/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

class RuiPlaceholder<BT>(
    override val ruiAdapter: RuiAdapter<BT>
) : RuiFragment<BT> {

    override val ruiParent = null

    override val ruiExternalPatch: (it: RuiFragment<BT>) -> Unit = { }

    val bridge = ruiAdapter.createPlaceholder()

    override fun ruiCreate() {

    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        bridge.add(this.bridge)
    }

    override fun ruiPatch() {

    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        bridge.remove(this.bridge)
    }

    override fun ruiDispose() {

    }

}