/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.adhoc

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.testing.*

//@Rui
//fun Basic(i : Int) {
//    T1(i)
//}

class RuiOptimizedBlock(
    ruiAdapter: RuiAdapter,
    override val fragments: Array<RuiFragment>,
    val patch: () -> Unit
) : RuiBlock(ruiAdapter) {
    override fun ruiPatch() {
        patch()
        super.ruiPatch()
    }
}

@Suppress("JoinDeclarationAndAssignment", "unused")
class OptimizedBlock(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    // -------------------------------------------------------------------------

    val ruiB0T10: RuiT1
    val ruiB0T01: RuiT0
    override val fragment0: RuiOptimizedBlock

    init {
        ruiB0T10 = RuiT1(ruiAdapter, v0)
        ruiB0T01 = RuiT0(ruiAdapter)
        fragment0 = RuiOptimizedBlock(
            ruiAdapter,
            arrayOf(ruiB0T10, ruiB0T01)
        ) {
            if (ruiDirty0 and 1 != 0) {
                ruiB0T10.p0 = v0
                ruiB0T10.ruiInvalidate0(1)
                ruiB0T10.ruiPatch()
            }
        }
    }

    // -------------------------------------------------------------------------


}