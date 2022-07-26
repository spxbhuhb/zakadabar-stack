/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiAnchor
import zakadabar.rui.runtime.RuiFragment

@Rui
fun T0() { }

@Suppress("unused")
open class RuiT0(
    ruiAdapter: RuiAdapter
) : RuiFragment(ruiAdapter), WithName {

    override val name = "T0"

    init {
        @Suppress("LeakingThis")
        RuiTestEvents.Init.report(this)
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this)
    }

    override fun ruiMount(anchor : RuiAnchor) {
        RuiTestEvents.Mount.report(this, "anchor=$anchor")
        super.ruiCreate()
    }

    override fun ruiPatch() {
        RuiTestEvents.Patch.report(this)
    }

    override fun ruiUnmount() {
        RuiTestEvents.Unmount.report(this)
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
    }
}

@Rui
fun T1(p0 : Int) { }

@Suppress("unused")
class RuiT1(
    ruiAdapter: RuiAdapter,
    var p0: Int
) : RuiFragment(ruiAdapter), WithName {

    override val name = "T1"

    var ruiDirty0 = 0

    @Suppress("unused")
    fun ruiInvalidate0(mask: Int) {
        RuiTestEvents.Invalidate.report(this, "mask:$mask dirty:$ruiDirty0 -> ${ruiDirty0 or mask}")
        ruiDirty0 = ruiDirty0 or mask
    }

    init {
        RuiTestEvents.Init.report(this)
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this, "value=$p0")
    }

    override fun ruiMount(anchor : RuiAnchor) {
        RuiTestEvents.Mount.report(this, "anchor=$anchor")
        super.ruiCreate()
    }

    override fun ruiPatch() {
        RuiTestEvents.Patch.report(this, "dirty=$ruiDirty0 value=$p0")
        ruiDirty0 = 0
    }

    override fun ruiUnmount() {
        RuiTestEvents.Unmount.report(this)
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
    }
}