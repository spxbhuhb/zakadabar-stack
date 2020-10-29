/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application.navigation

import kotlinx.browser.window
import org.w3c.dom.events.Event

/**
 * Handles changes of the browser window's location.
 * Provides methods for changing the location.
 */
object Navigation {

    const val EVENT = "z-navigation"

    lateinit var state: NavState

    fun init() {
        window.addEventListener("popstate", onPopState)
        state = NavState(window.location.pathname, window.location.search)
        state.layout.resume(state)
        window.dispatchEvent(Event(EVENT))
    }

    private val onPopState = fun(_: Event) {
        val newState = NavState(window.location.pathname, window.location.search)
        if (newState.layout !== state.layout) {
            state.layout.pause()
        }
        state = newState
        state.layout.resume(state)
        window.dispatchEvent(Event(EVENT))
    }

    fun changeLocation(path: String) {
        window.history.pushState("", "", path)
        state = NavState(path, "")
        window.dispatchEvent(Event(EVENT))
    }

    fun back() {

    }

}