/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.testing.*

@Suppress("JoinDeclarationAndAssignment", "unused")
class NonOptimizedBlock(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override val fragment0 = object : RuiBlock(ruiAdapter) {

        val ruiT10 = object : RuiT1(ruiAdapter, v0) {
            override fun ruiPatch() {
                if (ruiDirty0 and 1 != 0) {
                    p0 = v0
                    ruiInvalidate0(1)
                }
                super.ruiPatch()
            }
        }

        val ruiT01 = object : RuiT0(ruiAdapter) {

        }

        override val fragments: Array<RuiFragment> = arrayOf(
            ruiT10,
            ruiT01
        )
    }

}

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