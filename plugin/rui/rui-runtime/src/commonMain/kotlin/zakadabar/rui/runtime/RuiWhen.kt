/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

open class RuiWhen(
    adapter: RuiAdapter,
    ruiPatchState : (it : RuiFragment) -> Unit,
    val ruiSelect : () -> RuiFragment
) : RuiFragment(adapter, ruiPatchState) {

    var ruiFragment: RuiFragment

    init {
        ruiFragment = ruiSelect()
    }

    override fun ruiCreate() {
        ruiFragment.ruiCreate()
    }

    override fun ruiMount(anchor: RuiAnchor) {
        ruiFragment.ruiMount(anchor)
    }

    override fun ruiPatch() {
        val newFragment = ruiSelect()
        if (newFragment == ruiFragment) {
            ruiFragment.ruiPatch()
        } else {
            ruiFragment.ruiDispose()
            ruiFragment = newFragment
            ruiFragment.ruiCreate()
        }
    }

    override fun ruiUnmount() {
        ruiFragment.ruiUnmount()
    }

    override fun ruiDispose() {
        ruiFragment.ruiDispose()
    }

}