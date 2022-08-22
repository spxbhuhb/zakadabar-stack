/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.run

import zakadabar.rui.kotlin.plugin.RuiTest
import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.rui
import zakadabar.rui.runtime.testing.T1

@RuiTest
fun branchIfWithoutElse() {
    rui {
        ifWithoutElse(10)
    }
}

@Rui
fun ifWithoutElse(i : Int) {
    if (i == 0) T1(i)
}