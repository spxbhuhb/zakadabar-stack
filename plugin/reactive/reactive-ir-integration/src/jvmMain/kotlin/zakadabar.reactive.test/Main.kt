/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.reactive.core.*

fun A(callSiteOffset: Int, parentState: ReactiveState) {
    if (optimize0(callSiteOffset, parentState)) return

    parentState.future.last().handle = "A"

    parentState.lastChildCurrentToFuture()
}

fun B(value: Int, callSiteOffset: Int, parentState: ReactiveState) {
    if (optimize1(callSiteOffset, parentState, value)) return

    parentState.future.last().handle = "B: $value"

    parentState.lastChildCurrentToFuture()
}


fun main() {
    val rootState = ReactiveState(ReactiveContext(), "root", emptyArray())

    println("---- start ----")

    A(1, rootState)
    B(12, 2, rootState)
    rootState.currentToFuture()

    println("---- rerun: no change ----")

    A(1, rootState)
    B(12, 2, rootState)
    rootState.currentToFuture()

    println("---- rerun: after change ----")

    A(1, rootState)
    B(13, 2, rootState)
    rootState.currentToFuture()

    println("---- rerun: no change ----")

    A(1, rootState)
    B(13, 2, rootState)
    rootState.currentToFuture()

}