/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import zakadabar.rui.core.Rui
import zakadabar.rui.core.RuiBlock

@Rui
fun P0() {

}

class RuiPrimitiveBlock(
    var value: Int
) : RuiBlock() {

    init {
        println("================    Primitive.init: $value")
    }

    override var patch = {
        println("================    Primitive.patch: $value")
    }
}