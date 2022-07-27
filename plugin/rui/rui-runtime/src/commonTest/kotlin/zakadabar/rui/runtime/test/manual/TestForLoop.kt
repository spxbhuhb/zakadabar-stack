/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiFragment
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

    inner class RuiLambda1(ruiAdapter: RuiAdapter, v0: Int) : RuiT1(ruiAdapter, v0)

    inner class RuiForLoop1(ruiAdapter: RuiAdapter) : RuiFragment(ruiAdapter) {

        val fragments = mutableListOf<RuiLambda1>()

        override fun ruiCreate() {
            for (i in 1..10) {
                fragments.add(RuiLambda1(ruiAdapter, i))
            }
        }

        @Suppress("UseWithIndex")
        override fun ruiPatch() {
            var index = 0
            for (i in 1..10) {
                if (index >= fragments.size) {
                    val f = RuiLambda1(ruiAdapter, i)
                    fragments.add(f)
                    f.ruiCreate()
                } else {
                    val f = fragments[i]
                    f.p0 = i
                    f.ruiInvalidate0(1)
                    f.ruiPatch()
                }
                index ++
            }
            while (index < fragments.size) {
                val f = fragments.removeLast()
                f.ruiUnmount()
                f.ruiDispose()
                index ++
            }
        }

        override fun ruiDispose() {
            for (f in fragments) {
                f.ruiDispose()
            }
        }
    }

    override val fragment0 = RuiForLoop1(ruiAdapter)

}