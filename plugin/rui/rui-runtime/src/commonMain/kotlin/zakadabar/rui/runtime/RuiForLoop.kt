/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

class RuiForLoop(
    adapter: RuiAdapter,
    anchor: RuiFragment,
    val factory: RuiFragmentFactory
) : RuiFragment(adapter, anchor, {  }) {

    val fragments = mutableListOf<RuiFragment>()

    override fun ruiCreate() {

    }

    override fun ruiPatchRender() {

    }

    override fun ruiDispose() {
        for (fragment in fragments) {
            fragment.ruiDispose()
        }
    }
}