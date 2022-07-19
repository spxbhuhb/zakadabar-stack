/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import zakadabar.rui.runtime.RuiComponentBase

fun HigherOrderFun(builder: () -> Unit) {
    builder()
}

class RuiHigherOrderFunBase(
    var builder: () -> RuiComponentBase
): RuiComponentBase() {

    var block0 = builder()

    override fun patch(mask: Array<Int>) {
        block0.patch(arrayOf(0))
    }

}