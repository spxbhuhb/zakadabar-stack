/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

open class RuiBlock<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    vararg val fragments: RuiFragment<BT>
) : RuiFragment<BT> {

    override val ruiParent = null
    override val ruiExternalPatch: (it: RuiFragment<BT>) -> Unit = {  }

    override fun ruiCreate() {
        for (i in fragments.indices) {
            fragments[i].ruiCreate()
        }
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        for (i in fragments.indices) {
            fragments[i].ruiMount(bridge)
        }
    }

    override fun ruiPatch() {
        for (fragment in fragments) {
            fragment.ruiExternalPatch(fragment)
            fragment.ruiPatch()
        }
    }

    override fun ruiUnmount(bridge : RuiBridge<BT>) {
        for (i in fragments.indices) {
            fragments[i].ruiUnmount(bridge)
        }
    }

    override fun ruiDispose() {
        for (i in fragments.indices) {
            fragments[i].ruiDispose()
        }
    }

}