/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiFragmentFactory
import zakadabar.rui.runtime.RuiWhen

open class RuiTestWhen(
    adapter: RuiAdapter,
    anchor: RuiFragment?,
    select: () -> RuiFragmentFactory
) : RuiWhen(adapter, anchor, select), WithName {

    override val name = "WHEN"

    init {
        @Suppress("LeakingThis")
        RuiTestEvents.Init.report(this)
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this)
        super.ruiCreate()
    }

    override fun ruiPatchRender() {
        RuiTestEvents.PatchRender.report(this)
        super.ruiPatchRender()
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
        super.ruiDispose()
    }
}