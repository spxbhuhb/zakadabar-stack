/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiFragment


class RuiTestFragment(
    ruiAdapter: RuiAdapter,
    ruiAnchor: RuiFragment?,
    ruiPatchState: (it: RuiFragment) -> Unit,
    var value: Int
) : RuiFragment(ruiAdapter, ruiAnchor, ruiPatchState), WithName {

    override val name = "FRAGMENT"

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        RuiTestEvents.Invalidate.report(this, "mask:$mask dirty:$ruiDirty0 -> ${ruiDirty0 or mask}")
        ruiDirty0 = ruiDirty0 or mask
    }

    init {
        RuiTestEvents.Init.report(this)
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this, "value=$value")
    }

    override fun ruiPatchRender() {
        RuiTestEvents.PatchRender.report(this, "dirty=$ruiDirty0 value=$value")
        ruiDirty0 = 0
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
    }
}