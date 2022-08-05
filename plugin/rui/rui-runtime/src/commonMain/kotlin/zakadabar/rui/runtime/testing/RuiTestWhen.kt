/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiAnchor
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiWhen

abstract class RuiTestWhen(
    adapter: RuiAdapter,
    ruiPatchState : (it : RuiFragment) -> Unit,
    ruiSelect : () -> RuiFragment
) : RuiWhen(adapter, ruiPatchState, ruiSelect), WithName {

    override val name = "WHEN"

    init {
        @Suppress("LeakingThis")
        RuiTestEvents.Init.report(this)
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this)
        super.ruiCreate()
    }

    override fun ruiMount(anchor : RuiAnchor) {
        RuiTestEvents.Mount.report(this, "anchor=$anchor")
        super.ruiCreate()
    }

    override fun ruiPatch() {
        RuiTestEvents.Patch.report(this)
        super.ruiPatch()
    }

    override fun ruiUnmount() {
        RuiTestEvents.Unmount.report(this)
        super.ruiUnmount()
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
        super.ruiDispose()
    }
}