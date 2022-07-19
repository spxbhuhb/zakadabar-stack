/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.errors

import zakadabar.rui.kotlin.plugin.P1
import zakadabar.rui.runtime.Rui

@Rui
fun Variables(i : Int, s : String) {
    val i2 = 12

    P1(0)
    P1(i)
    P1(i2)
}