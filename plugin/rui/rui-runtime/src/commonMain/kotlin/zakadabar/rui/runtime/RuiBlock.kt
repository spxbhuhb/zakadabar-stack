/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

open class RuiBlock(
    adapter: RuiAdapter,
    anchor: RuiFragment?,
    val fragments: Array<RuiFragment>
) : RuiFragment(adapter, anchor, {  }) {

    override fun ruiCreate() {
        for (i in fragments.indices) {
            fragments[i].ruiCreate()
        }
    }

    override fun ruiPatchRender() {
        for (i in fragments.indices) {
            fragments[i].apply {
                ruiPatchState(this)
                ruiPatchRender()
            }
        }
    }

    override fun ruiDispose() {
        for (i in fragments.indices) {
            fragments[i].ruiDispose()
        }
    }
}