/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

open class RuiWhen(
    adapter: RuiAdapter,
    anchor: RuiFragment?,
    val select: () -> RuiFragmentFactory
) : RuiFragment(adapter, anchor, {  }) {

    var fragmentFactory = select()
    var fragment = fragmentFactory.func(ruiAdapter, ruiAnchor)

    override fun ruiCreate() {
        fragment.ruiCreate()
    }

    override fun ruiPatchRender() {
        val newFactory = select()
        if (newFactory == fragmentFactory) {
            fragment.ruiPatchState(fragment)
            fragment.ruiPatchRender()
        } else {
            fragment.ruiDispose()
            fragmentFactory = newFactory
            fragment = fragmentFactory.func(ruiAdapter, ruiAnchor)
            fragment.ruiCreate()
        }
    }

    override fun ruiDispose() {
        fragment.ruiDispose()
    }
}