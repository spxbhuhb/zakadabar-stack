/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment

abstract class RuiTestBlock(
    adapter: RuiAdapter,
    vararg fragments : RuiFragment
) : RuiBlock(adapter, *fragments), WithName {

    override val name = "BLOCK"

    init {
        @Suppress("LeakingThis")
        RuiTestEvents.Init.report(this)
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this)
        super.ruiCreate()
    }

    override fun ruiPatch() {
        RuiTestEvents.Patch.report(this)
        super.ruiPatch()
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
        super.ruiDispose()
    }
}