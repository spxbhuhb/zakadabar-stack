/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiComponentBase

fun Primitive(value: Int) {
    // this is just a mock primitive, that prints out happenings
}

class RuiPrimitive(
    var value: Int
) : RuiComponentBase() {

    init {
        println("================    Primitive.init: $value")
    }

    override fun patch(mask: Array<Int>) {
        println("================    Primitive.patch: ${mask[0]} $value")
    }
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