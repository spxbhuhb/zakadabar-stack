/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.original

import zakadabar.rui.runtime.Rui

@Rui
fun Use() {
    Comp() value "a"
}

@Rui
fun Comp(value: String = ""): TransformComp {
    // Text(value)
    return TransformComp
}

object TransformComp

infix fun TransformComp.value(v: String) = Unit


@Rui
fun Wrapper(@Rui block: TransformBlock.() -> Unit) {
    var name = "block"
    //Text("before the $block")
    TransformBlock.block()
    //Text("after the $block")
}

object TransformBlock {
    var name: String = ""
}

@Rui
fun Counter() {
    var count = 0
    Wrapper {
        name = "Wrapped Block"
        // Button { "click count: $count" } onClick { count ++ }
    }
}

