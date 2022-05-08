/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.core

annotation class Reactive

open class ReactiveContext {

    fun clear(state: ReactiveState) {
        println("clear: ${state.owner}")
    }

    fun add(childState: ReactiveState) {
        println("add: ${childState.owner}")
    }

    fun remove(childState: ReactiveState) {
        println("remove: ${childState.owner}")
    }
}

open class ReactiveState(
    val reactiveContext: ReactiveContext,
    val key: String,
    val owner: String,
    var receivedValues: Array<Any?>
) {
    var handle: Any? = null
    var current = emptyList<ReactiveState>()
    var future = mutableListOf<ReactiveState>()

    fun get(key: String): ReactiveState? = current.firstOrNull { it.key == key }

    fun put(state: ReactiveState) {
        future += state
    }

    fun lastChildCurrentToFuture() {
        future.last().currentToFuture()
    }

    /**
     * Transforms the render stored in [ReactiveState.current] into the render stored in
     * [ReactiveState.future]. This operation is executed on the last future child state.
     */
    fun currentToFuture() {
        reactiveContext.clear(this)
        future.forEach { child ->
            reactiveContext.add(child)
        }
        current = future
        future = mutableListOf()
    }
}

fun optimize(owner : String, callSiteOffset: Int, parentState: ReactiveState): ReactiveState? {
    val key = callSiteOffset.toString()
    val state = parentState.get(key)

    if (state == null) { // new state -> have to render
        return ReactiveState(parentState.reactiveContext, key, owner, emptyArray()).also { parentState.put(it) }
    }

    // already has a state with no parameters -> don't have to render
    parentState.put(state)
    return null
}

fun optimize(owner : String, callSiteOffset: Int, parentState: ReactiveState, p0: Any?): ReactiveState? {
    val key = callSiteOffset.toString()
    val state = parentState.get(key)

    if (state == null) { // new state -> have to render
        return ReactiveState(parentState.reactiveContext, key, owner, arrayOf(p0)).also { parentState.put(it) }
    }

    // already has a state compare values with previous ones
    parentState.put(state)

    if (state.receivedValues[0] == p0) {
        return null // same state, don't have to render
    }

    // different values, have to render, copy new values into the state
    state.future.clear()
    state.receivedValues = arrayOf(p0)
    return state
}

fun lastChildCurrentToFuture(parentState : ReactiveState) {
    parentState.lastChildCurrentToFuture()
}

fun A(callSiteOffset: Int, parentState: ReactiveState) {
    if (optimize("A", callSiteOffset, parentState) == null) return

    parentState.future.last().handle = "A"

    parentState.lastChildCurrentToFuture()
}

fun B(value: Int, callSiteOffset: Int, parentState: ReactiveState) {
    if (optimize("A", callSiteOffset, parentState, value) == null) return

    parentState.future.last().handle = "B: $value"

    parentState.lastChildCurrentToFuture()
}


fun main() {
    val rootState = ReactiveState(ReactiveContext(), "root", "root", emptyArray())

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