/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiAnchor
import zakadabar.rui.runtime.RuiFragment

open class RuiTestEmptyFragment(
    ruiAdapter: RuiAdapter
) : RuiFragment(ruiAdapter, {  }), WithName {

    override val name = "EMPTY"

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