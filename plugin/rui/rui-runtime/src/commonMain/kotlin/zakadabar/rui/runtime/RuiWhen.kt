/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

abstract class RuiWhen(
    adapter: RuiAdapter
) : RuiFragment(adapter) {

    abstract var ruiFragment: RuiFragment

    abstract fun ruiSelect(): RuiFragment

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