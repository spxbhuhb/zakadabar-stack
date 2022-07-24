/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiFragmentFactory
import zakadabar.rui.runtime.testing.*

class TestBranch(
    // state variables from function parameters
    var value: Int,
    override val name: String = "BRANCH"
) : RuiFragment(RuiTestAdapter, null, { }), WithName {

    // state variables from top-level declarations
    var v2 = 12

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
        ruiBlock0.ruiCreate()
    }

    override fun ruiPatchRender() {
        RuiTestEvents.PatchRender.report(this)
        ruiBlock0.ruiPatchRender()
    }

    override fun ruiDispose() {
        RuiTestEvents.Dispose.report(this)
        ruiBlock0.ruiDispose()
    }

    val ruiBranch4 = RuiFragmentFactory { adapter, anchor ->
        RuiTestFragment(adapter, anchor, {
            it as RuiTestFragment
            if (ruiDirty0 and 1 != 0) {
                it.value = value + 20
                it.ruiInvalidate0(1)
            }
        }, value + 20)
    }

    val ruiBranch3 = RuiFragmentFactory { adapter, anchor ->
        RuiTestFragment(adapter, anchor, {
            it as RuiTestFragment
            if (ruiDirty0 and 1 != 0) {
                it.value = value + 10
                it.ruiInvalidate0(1)
            }
        }, value + 10)
    }

    val ruiWhen2 = RuiTestWhen(ruiAdapter, ruiAnchor) {
        when (value) {
            1 -> ruiBranch3
            2 -> ruiBranch4
            else -> ruiAdapter.emptyFragmentFactory
        }
    }

    val ruiTestFragment1 = RuiTestFragment(ruiAdapter, ruiAnchor, {
        it as RuiTestFragment
        if (ruiDirty0 and 1 != 0) {
            it.value = value
            it.ruiInvalidate0(1)
        }
    }, value)

    val ruiBlock0 = RuiTestBlock(
        ruiAdapter,
        ruiAnchor,
        arrayOf(
            ruiTestFragment1,
            ruiWhen2
        )
    )

}