/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.reactive.core.Reactive
import zakadabar.reactive.core.ReactiveContext
import zakadabar.reactive.core.ReactiveState


@Reactive
fun d(callSiteOffset : Int, parentState : ReactiveState) {
    c(14)
    c(15)
}

@Reactive
fun c(a : Int) {
    println("called c($a)")
}

fun main() {
    ReactiveState(ReactiveContext(), "<root>", "<root>", emptyArray()).apply {
        d(0, this)
    }
}