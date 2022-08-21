/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.test

import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.rui
import zakadabar.rui.runtime.testing.RuiTestAdapter
import zakadabar.rui.runtime.testing.T1


@Rui
fun R1(value: Int) {
    T1(12)
}

fun main() {
    rui(RuiTestAdapter()) {
        R1(12)
    }
}