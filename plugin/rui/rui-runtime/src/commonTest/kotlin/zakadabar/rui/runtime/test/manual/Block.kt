/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.testing.RuiC1
import zakadabar.rui.runtime.testing.RuiT0
import zakadabar.rui.runtime.testing.RuiT1
import zakadabar.rui.runtime.testing.TestNode

@Suppress("JoinDeclarationAndAssignment", "unused")
class Block(
    ruiAdapter : RuiAdapter<TestNode>
) : RuiC1(ruiAdapter, {  }) {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    val ruiB0T10: RuiT1<TestNode>
    val ruiB0T01: RuiT0<TestNode>
    override val fragment0: RuiBlock<TestNode>

    init {
        ruiB0T10 = RuiT1(ruiAdapter, {
            it as RuiT1
            if (ruiDirty0 and 1 != 0) {
                it.p0 = v0
                it.ruiInvalidate0(1)
                it.ruiPatch()
            }
        }, v0)

        ruiB0T01 = RuiT0(ruiAdapter, {})
        fragment0 = RuiBlock(
            ruiAdapter,
            ruiB0T10,
            ruiB0T01
        )
    }

}