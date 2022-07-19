/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.errors

import zakadabar.rui.kotlin.plugin.P1
import zakadabar.rui.runtime.Rui

@Rui
fun Basic(i : Int) {
    val i2 = 12
    P1(0)
    P1(i)
    P1(i2)
    P1(i + i2)
    if (i == 1) {
        P1(i2)
    }
    when {
        i == 1 -> P1(i2 + 1)
        i == 2 -> P1(i2 + 2)
    }
}