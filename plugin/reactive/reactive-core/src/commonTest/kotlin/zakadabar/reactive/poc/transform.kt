/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

import zakadabar.reactive.core.Reactive

@Reactive
fun Use() {
    Comp() value "a"
}

@Reactive
fun Comp(value: String = ""): TransformComp {
    // Text(value)
    return TransformComp
}

object TransformComp

infix fun TransformComp.value(v: String) = Unit


@Reactive
fun Wrapper(@Reactive block: TransformBlock.() -> Unit) {
    var name = "block"
    //Text("before the $block")
    TransformBlock.block()
    //Text("after the $block")
}

object TransformBlock {
    var name: String = ""
}

@Reactive
fun Counter() {
    var count = 0
    Wrapper {
        name = "Wrapped Block"
        // Button { "click count: $count" } onClick { count ++ }
    }
}

