/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.original

import zakadabar.rui.runtime.testing.T0
import zakadabar.rui.runtime.testing.T1

fun Statements(
    s0 : Int
) {
    {
        T0()
        T1(s0)
    }

    if (s0 == 1) T1(s0)

    if (s0 == 1) {
        T1(s0)
    }

    if (s0 == 1) T1(s0) else T1(s0)

    when {
        s0 == 1 -> T1(s0)
        else -> T1(s0)
    }

    for (i in 1..10) {
        T1(s0 + i)
    }


}

fun Text(content: String) {
    // this is just a mock primitive, that prints out happenings
}

fun Div(builder: () -> Unit) {
    builder()
}

fun Blocks(value : Int) {
    Text("1")

    if (value % 2 == 0) {
        Text("even")
    } else {
        Text("odd")
    }

    for (i in 0..value) {
        Text("loop: $i")
    }

    Div {
        Text("inner")
    }
}