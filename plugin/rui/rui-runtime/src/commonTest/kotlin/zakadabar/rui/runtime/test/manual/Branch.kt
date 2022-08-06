/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiWhen
import zakadabar.rui.runtime.testing.*

@Suppress("JoinDeclarationAndAssignment", "unused")
class NonOptimizedBranch(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0: Int = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override val fragment0 = object : RuiTestWhen(ruiAdapter) {

        override var ruiFragment = ruiSelect()

        override fun ruiSelect(): RuiFragment =
            when (v0) {
                1 -> ruiBranch3
                2 -> ruiBranch4
                else -> ruiBranch5
            }

        val ruiBranch3 = object : RuiT1(ruiAdapter, v0 + 10) {
            override fun ruiPatch() {
                if (ruiDirty0 and 1 != 0) {
                    this.p0 = v0 + 10
                    ruiInvalidate0(1)
                }
                super.ruiPatch()
            }
        }

        val ruiBranch4 = object : RuiT1(ruiAdapter, v0 + 20) {
            override fun ruiPatch() {
                if (ruiDirty0 and 1 != 0) {
                    this.p0 = v0 + 20
                    ruiInvalidate0(1)
                }
                super.ruiPatch()
            }
        }

        val ruiBranch5 = RuiTestEmptyFragment(ruiAdapter)

    }

}

class RuiFragmentAndPatch<T : RuiFragment>(
    val fragment: T,
    val patch: (fragment: T) -> Unit
)

class RuiOptimizedWhen(
    ruiAdapter: RuiAdapter,
    val select: () -> RuiFragmentAndPatch<*>
) : RuiWhen(ruiAdapter) {
    override var ruiFragment: RuiFragment
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun ruiSelect(): RuiFragment {
        TODO("Not yet implemented")
    }
}

@Suppress("JoinDeclarationAndAssignment", "unused")
class OptimizedBranch(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0: Int = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    val ruiBranch3: RuiFragmentAndPatch<RuiT1>
    val ruiBranch4: RuiFragmentAndPatch<RuiT1>
    val ruiBranch5: RuiFragmentAndPatch<RuiTestEmptyFragment>
    override val fragment0: RuiOptimizedWhen

    init {
        ruiBranch3 = RuiFragmentAndPatch(RuiT1(ruiAdapter, v0 + 10)) {
            if (ruiDirty0 and 1 != 0) {
                it.p0 = v0 + 10
                ruiInvalidate0(1)
            }
        }

        ruiBranch4 = RuiFragmentAndPatch(RuiT1(ruiAdapter, v0 + 20)) {
            if (ruiDirty0 and 1 != 0) {
                it.p0 = v0 + 20
                ruiInvalidate0(1)
            }
        }

        ruiBranch5 = RuiFragmentAndPatch(RuiTestEmptyFragment(ruiAdapter)) {  }

        fragment0 = RuiOptimizedWhen(ruiAdapter) {
            when (v0) {
                1 -> ruiBranch3
                2 -> ruiBranch4
                else -> ruiBranch5
            }
        }
    }

}

