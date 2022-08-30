/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.*
import zakadabar.rui.runtime.testing.RuiT0
import zakadabar.rui.runtime.testing.RuiT1
import zakadabar.rui.runtime.testing.TestNode

@Suppress("unused")
class TestForLoop(
    override val ruiAdapter: RuiAdapter<TestNode>
) : RuiGeneratedFragment<TestNode> {

    override val ruiParent: RuiFragment<TestNode>? = null
    override val ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit = {  }

    override val fragment: RuiFragment<TestNode>

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    fun ruiEp1(it : RuiFragment<TestNode>) {
        it as RuiT1
        if (ruiDirty0 and 1 != 0) {
            it.p0 = v0
            it.ruiInvalidate0(1)
        }
    }

    fun ruiIterator0() = IntRange(0, 10).iterator()
    fun ruiBuilder0() =
        RuiBlock(
            ruiAdapter,
            RuiT1(ruiAdapter, this, ::ruiEp1, v0),
            RuiT0(ruiAdapter, this, {  })
        )


    init {
        fragment = RuiLoop(
            ruiAdapter,
            ::ruiIterator0,
            ::ruiBuilder0
        )
    }
}