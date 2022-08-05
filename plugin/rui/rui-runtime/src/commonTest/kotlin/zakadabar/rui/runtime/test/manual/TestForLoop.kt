/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiLoop
import zakadabar.rui.runtime.testing.*

@Suppress("unused")
class TestForLoop(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter(), { }), WithName {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override val fragment0: RuiFragment

    init {
        fragment0 = RuiLoop(
            ruiAdapter,
            { },
            { IntRange(0, 10).iterator() },
            {
                RuiBlock(
                    ruiAdapter,
                    RuiT1(ruiAdapter, {
                        it as RuiT1
                        if (ruiDirty0 and 1 != 0) {
                            it.p0 = v0
                            it.ruiInvalidate0(1)
                        }
                    }, v0),
                    RuiT0(ruiAdapter) { }
                )
            }
        )
    }
}