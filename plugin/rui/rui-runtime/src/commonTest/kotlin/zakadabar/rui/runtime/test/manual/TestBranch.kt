/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.testing.*

class TestBranch(
    // state variables from function parameters
    var value: Int,
    override val name: String = "BRANCH"
) : RuiBlock(RuiTestAdapter), WithName {

    var ruiDirty0 = 0

    init {
        @Suppress("LeakingThis")
        RuiTestEvents.Init.report(this)
    }

    fun ruiInvalidate0(mask: Int) {
        RuiTestEvents.Invalidate.report(this, "mask:$mask dirty:$ruiDirty0 -> ${ruiDirty0 or mask}")
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiCreate() {
        RuiTestEvents.Create.report(this)
        super.ruiCreate()
    }

    override fun ruiPatch() {
        RuiTestEvents.Patch.report(this)
        if (ruiDirty0 and 1 != 0) {
            ruiTestFragment0.value = value
            ruiTestFragment0.ruiInvalidate0(1)
        }
        super.ruiPatch()
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
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