/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiAdapterFactory

object RuiTestAdapterFactory : RuiAdapterFactory() {

    override fun accept(vararg args: Any?): RuiAdapter<TestNode> {
        return RuiTestAdapter()
    }

}