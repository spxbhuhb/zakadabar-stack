/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.adhoc

import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.testing.RuiTestAdapter
import zakadabar.rui.runtime.testing.RuiTestEmptyFragment
import zakadabar.rui.runtime.testing.RuiTestFragment
import zakadabar.rui.runtime.testing.RuiTestWhen

//@Rui
//fun Basic(i : Int) {
//    T1(i)
//}


class TestBranch(
    var value: Int
) : RuiBlock(RuiTestAdapter) {

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch() {
        if (ruiDirty0 and 1 != 0) {
            ruiTestFragment0.value = value
            ruiTestFragment0.ruiInvalidate0(1)
        }
        super.ruiPatch()
    }

    val ruiTestFragment0 = RuiTestFragment(ruiAdapter, value)

    val ruiBranch3 = object : RuiTestFragment(ruiAdapter, value + 10) {
        override fun ruiPatch() {
            if (ruiDirty0 and 1 != 0) {
                this.value = this@TestBranch.value + 10
                ruiInvalidate0(1)
            }
            super.ruiPatch()
        }
    }

    val ruiBranch4 = object : RuiTestFragment(ruiAdapter, value + 20) {
        override fun ruiPatch() {
            if (ruiDirty0 and 1 != 0) {
                this.value = this@TestBranch.value + 20
                ruiInvalidate0(1)
            }
            super.ruiPatch()
        }
    }

    val ruiBranch5 = RuiTestEmptyFragment(ruiAdapter)

    val ruiWhen0 = object : RuiTestWhen(ruiAdapter) {

        override var ruiFragment = ruiSelect()

        override fun ruiSelect(): RuiFragment =
            when (value) {
                1 -> ruiBranch3
                2 -> ruiBranch4
                else -> ruiBranch5
            }
    }

    override val fragments: Array<RuiFragment> = arrayOf(
        ruiTestFragment0,
        ruiWhen0
    )

}