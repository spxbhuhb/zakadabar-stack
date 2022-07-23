/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiFragment

@Rui
fun P0() {

}

@Rui
fun P1(p0: Int) {

}

@Rui
fun P2(p0: Int, p1: Int) {

}

class RuiPrimitiveFragment2(
    val adapter: RuiAdapter,
    val anchor: RuiFragment,
    var value: Int
) : RuiFragment() {

    var dirty0 = 0

    init {
        println("================    Primitive.init: $value")
    }

    override var ruiPatch = {
        println("================    Primitive.patch: $value")
    }

    fun invalidate0(index: Int) {

    }
}

class RuiPrimitiveFragment(
    var value: Int
) : RuiFragment() {

    var dirty0 = 0

    init {
        println("================    Primitive.init: $value")
    }

    override var ruiPatch = {
        println("================    Primitive.patch: $value")
    }

    fun invalidate0(index: Int) {

    }
}