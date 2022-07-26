/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiAnchor
import zakadabar.rui.runtime.RuiFragment


open class RuiTestFragment(
    ruiAdapter: RuiAdapter,
    var value: Int
) : RuiFragment(ruiAdapter), WithName {

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

    override fun ruiMount(anchor : RuiAnchor) {
        RuiTestEvents.Mount.report(this, "anchor=$anchor")
        super.ruiCreate()
    }

    override fun ruiPatch() {
        RuiTestEvents.Patch.report(this, "dirty=$ruiDirty0 value=$value")
        ruiDirty0 = 0
    }

    override fun ruiUnmount() {
        RuiTestEvents.Unmount.report(this)
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
    }
}