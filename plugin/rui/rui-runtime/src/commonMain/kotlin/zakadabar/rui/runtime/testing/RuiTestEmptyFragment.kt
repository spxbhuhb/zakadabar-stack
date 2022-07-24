/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiFragment

open class RuiTestEmptyFragment(
    ruiAdapter: RuiAdapter,
    ruiAnchor: RuiFragment?,
    ruiPatchState : (it : RuiFragment) -> Unit
) : RuiFragment(ruiAdapter, ruiAnchor, ruiPatchState), WithName {

    override val name = "EMPTY"

    init {
        @Suppress("LeakingThis")
        RuiTestEvents.Init.report(this)
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this)
    }

    override fun ruiPatchRender() {
        RuiTestEvents.PatchRender.report(this)
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
    }
}