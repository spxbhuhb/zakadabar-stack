/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

@Rui
fun R1(p0: Int) {

}

class RuiR1(
    val adapter: RuiAdapter,
    val anchor: RuiFragment,
    var value: Int
) : RuiFragment() {

    init {
        println("================    Primitive.init: $value")
    }

    override var patch = {
        println("================    Primitive.patch: $value")
    }

    var dirty0 = 0

    fun invalidate0(mask: Int) {
        dirty0 = dirty0 or mask
    }


}