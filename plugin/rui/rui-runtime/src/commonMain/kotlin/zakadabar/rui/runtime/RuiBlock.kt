/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

abstract class RuiBlock(
    adapter: RuiAdapter
) : RuiFragment(adapter) {

    abstract val fragments: Array<RuiFragment>

    override fun ruiCreate() {
        for (i in fragments.indices) {
            fragments[i].ruiCreate()
        }
    }

    override fun ruiMount(anchor: RuiAnchor) {
        for (i in fragments.indices) {
            fragments[i].ruiMount(anchor)
        }
    }

    override fun ruiPatch() {
        for (i in fragments.indices) {
            fragments[i].ruiPatch()
        }
    }

    override fun ruiUnmount() {
        for (i in fragments.indices) {
            fragments[i].ruiUnmount()
        }
    }

    override fun ruiDispose() {
        for (i in fragments.indices) {
            fragments[i].ruiDispose()
        }
    }

}