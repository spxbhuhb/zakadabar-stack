/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.testing.*

class TestBranch(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0: Int = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    val ruiBranch3 = object : RuiT1(ruiAdapter, v0 + 10) {
        override fun ruiPatch() {
            if (ruiDirty0 and 1 != 0) {
                this.p0 = this@TestBranch.v0 + 10
                ruiInvalidate0(1)
            }
            super.ruiPatch()
        }
    }

    val ruiBranch4 = object : RuiT1(ruiAdapter, v0 + 20) {
        override fun ruiPatch() {
            if (ruiDirty0 and 1 != 0) {
                this.p0 = this@TestBranch.v0 + 20
                ruiInvalidate0(1)
            }
            super.ruiPatch()
        }
    }

    val ruiBranch5 = RuiTestEmptyFragment(ruiAdapter)

    override val fragment0 = object : RuiTestWhen(ruiAdapter) {

        override var ruiFragment = ruiSelect()

        override fun ruiSelect(): RuiFragment =
            when (v0) {
                1 -> ruiBranch3
                2 -> ruiBranch4
                else -> ruiBranch5
            }
    }

}