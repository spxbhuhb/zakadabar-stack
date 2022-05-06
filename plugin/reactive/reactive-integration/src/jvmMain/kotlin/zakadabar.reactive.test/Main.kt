/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.reactive.core.Reactive

fun d() {
    c(12)
    c(13)
}

@Reactive
fun c(a : Int, td : Int = 20) {
    println("called c(" + a.toString() + ")")
}

fun whatever(callSiteOffset : Int) {
    println("called whatever: " + callSiteOffset)
}

fun main() {
    d()
}