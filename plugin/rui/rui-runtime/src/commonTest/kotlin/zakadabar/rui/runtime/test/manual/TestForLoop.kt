/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiLoop
import zakadabar.rui.runtime.testing.RuiC1
import zakadabar.rui.runtime.testing.RuiT1
import zakadabar.rui.runtime.testing.RuiTestAdapter
import zakadabar.rui.runtime.testing.WithName

class TestForLoop(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override val fragment0 = object : RuiLoop<Int>(ruiAdapter) {

        override fun makeIterator(): Iterator<Int> = IntRange(0, 10).iterator()

        override fun makeFragment() = object : RuiBlock(ruiAdapter) {

            val ruiT10 = RuiT1(ruiAdapter, loopVariable!!)
            val ruiT01 = RuiT1(ruiAdapter, v0)

            override val fragments: Array<RuiFragment> = arrayOf(
                ruiT10,
                ruiT01
            )

            override fun ruiPatch() {
                if (ruiT10.p0 != loopVariable) {
                    ruiT10.p0 = loopVariable!!
                    ruiT10.ruiInvalidate0(1)
                }
                if (ruiDirty0 and 1 != 0) {
                    ruiT01.p0 = v0
                    ruiT01.ruiInvalidate0(1)
                }
                super.ruiPatch()
            }

        }

    }

}