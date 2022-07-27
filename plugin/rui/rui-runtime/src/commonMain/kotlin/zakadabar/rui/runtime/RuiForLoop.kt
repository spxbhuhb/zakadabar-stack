/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

class RuiForLoop(
    ruiAdapter: RuiAdapter,
) : RuiFragment(ruiAdapter) {

    val fragments = mutableListOf<RuiFragment>()

    override fun ruiCreate() {

    }

    override fun ruiPatch() {

    }

    override fun ruiDispose() {
        for (fragment in fragments) {
            fragment.ruiDispose()
        }
    }
}