/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiEmptyFragment
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiWhen
import zakadabar.rui.runtime.testing.RuiC1
import zakadabar.rui.runtime.testing.RuiT1
import zakadabar.rui.runtime.testing.RuiTestAdapter
import zakadabar.rui.runtime.testing.WithName

@Suppress("JoinDeclarationAndAssignment")
class TestBranch(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter(), { }), WithName {

    var v0: Int = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    val ruiBranch3: RuiT1
    val ruiBranch4: RuiT1
    val ruiBranch5: RuiEmptyFragment

    override val fragment0: RuiFragment

    init {
        ruiBranch3 = RuiT1(ruiAdapter, {
            it as RuiT1
            if (ruiDirty0 and 1 != 0) {
                it.p0 = v0 + 10
                it.ruiInvalidate0(1)
            }
            super.ruiPatch()
        }, v0 + 10)

        ruiBranch4 = RuiT1(ruiAdapter, {
            it as RuiT1
            if (ruiDirty0 and 1 != 0) {
                it.p0 = v0 + 20
                it.ruiInvalidate0(1)
            }
        }, v0 + 20)

        ruiBranch5 = RuiEmptyFragment(ruiAdapter)

        fragment0 = RuiWhen(
            ruiAdapter,
            { },
            {
                when (v0) {
                    1 -> ruiBranch3
                    2 -> ruiBranch4
                    else -> ruiBranch5
                }
            }
        )
    }
}