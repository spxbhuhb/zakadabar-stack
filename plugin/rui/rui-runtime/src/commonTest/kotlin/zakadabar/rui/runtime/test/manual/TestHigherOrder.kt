/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.testing.*

class TestHigherOrder: RuiC1(RuiTestAdapter(), {  }) {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask : Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    inner class RuiLambda1(ruiAdapter: RuiAdapter<TestNode>) : RuiT0<TestNode>(ruiAdapter, {})

    override val fragment0 = RuiH1(ruiAdapter, {  }) { ruiAdapter -> RuiLambda1(ruiAdapter) }

}