/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.run

import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.rui
import zakadabar.rui.runtime.testing.T1

fun test() {
    rui {
        call(10)
    }
}

@Rui
fun call(i : Int) {
    T1(i + 1)
    T1(i + 2)
}