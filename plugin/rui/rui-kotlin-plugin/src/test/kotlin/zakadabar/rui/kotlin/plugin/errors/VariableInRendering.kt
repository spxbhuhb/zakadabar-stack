/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.errors

import zakadabar.rui.kotlin.plugin.P0
import zakadabar.rui.runtime.Rui

@Rui
fun VariableInRendering() {
    P0()
    var i = 0 // error 0001 : variable declaration is not allowed in rendering
    P0()
}