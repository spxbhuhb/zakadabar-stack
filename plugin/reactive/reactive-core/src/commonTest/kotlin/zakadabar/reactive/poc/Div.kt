/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

import zakadabar.reactive.core.Component

fun Div(builder: () -> Unit) {
    builder()
}

fun ReactiveDiv(builder: () -> Component): Component {

    var builder0 = builder

    var dirty0 = 0
    val self0 = Component()

    var content0: Component? = null

    self0.c = {
        content0 = builder0()
        content0 !!.c()
    }

    self0.p = { _: Int ->
        content0 !!.p(0)
    }

    return self0
}