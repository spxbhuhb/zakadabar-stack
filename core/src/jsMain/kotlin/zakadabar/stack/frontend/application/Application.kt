/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.window
import org.w3c.dom.events.Event

/**
 * The application that runs in the browser window. This object contains data
 * and resources that are used all over the application.
 */
object Application {

    lateinit var routing: AppRouting

    const val EVENT = "zk-navstate-change"

    fun init() {
        window.addEventListener("popstate", onPopState)
        routing.onNavStateChange(NavState(window.location.pathname, window.location.search))
        window.dispatchEvent(Event(EVENT))
    }

    private val onPopState = fun(_: Event) {
        routing.onNavStateChange(NavState(window.location.pathname, window.location.search))
        window.dispatchEvent(Event(EVENT))
    }

    fun changeNavState(path: String, query: String = "") {
        val url = if (query.isEmpty()) path else "$path?$query"
        window.history.pushState("", "", url)
        routing.onNavStateChange(NavState(path, query))
        window.dispatchEvent(Event(EVENT))
    }

    fun back() {

    }
}