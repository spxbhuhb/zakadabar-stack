/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import zakadabar.rui.runtime.RuiComponentBase
import zakadabar.rui.runtime.RuiFragment

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

class RuiPrimitiveFragment(
    var value: Int
) : RuiFragment() {

    init {
        println("================    Primitive.init: $value")
    }

    override var patch = {
        println("================    Primitive.patch: $value")
    }
}