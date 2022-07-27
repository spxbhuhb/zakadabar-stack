/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.testing.*

class TestHigherOrder(
    override val name: String = "<root>"
) : RuiC1(RuiTestAdapter), WithName {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask : Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    inner class RuiLambda1(ruiAdapter: RuiAdapter) : RuiT0(ruiAdapter)

    override val fragment0 = RuiH1(ruiAdapter) { ruiAdapter -> RuiLambda1(ruiAdapter) }

}