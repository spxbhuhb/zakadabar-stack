/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.testing.*

class TestBlock(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask : Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override val fragment0 = object : RuiBlock(ruiAdapter) {

        val ruiT10 = RuiT1(ruiAdapter, v0)
        val ruiT01 = RuiT0(ruiAdapter)

        override val fragments: Array<RuiFragment> = arrayOf(
            ruiT10,
            ruiT01
        )

        override fun ruiPatch() {
            if (ruiDirty0 and 1 != 0) {
                ruiT10.p0 = v0
                ruiT10.ruiInvalidate0(1)
                ruiT10.ruiPatch()
            }
        }
    }

}