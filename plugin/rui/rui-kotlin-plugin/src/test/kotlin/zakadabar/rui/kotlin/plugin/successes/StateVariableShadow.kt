/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.successes

import zakadabar.rui.core.Rui

@Suppress("unused", "TestFunctionName")
@Rui
fun StateVariableShadow(v : Int) {
    if (v == 1) {
        @Suppress("NAME_SHADOWING")
        val v = "s" // I would say this is a lesser kind of perversion...
        println(v)
    }
    @Suppress("UNUSED_VARIABLE", "NAME_SHADOWING")
    var v = v.toString() // I would say this is a greater kind of perversion...
}