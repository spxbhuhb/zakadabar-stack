/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

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