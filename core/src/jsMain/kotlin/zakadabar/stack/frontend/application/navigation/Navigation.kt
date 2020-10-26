/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application.navigation

import kotlinx.browser.window
import org.w3c.dom.events.Event
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.PublicApi

/**
 * Handles changes of the browser window's location.
 * Provides methods for changing the location.
 */
object Navigation {

    const val CREATE = "create"
    const val READ = "read"
    const val UPDATE = "update"
    const val DELETE = "delete"
    const val ALL = "all"

    const val EVENT = "z-navigation"

    lateinit var state : NavState

    fun init() {
        window.addEventListener("popstate", onPopState)
        state = NavState(window.location.pathname, window.location.search)
        window.dispatchEvent(Event(EVENT))
    }

    private val onPopState = fun(_: Event) {
        state = NavState(window.location.pathname, window.location.search)
        window.dispatchEvent(Event(EVENT))
    }

    @PublicApi
    fun changeLocation(dto: RecordDto<*>, view: String) {
        val path = "/view/${dto.getType()}/${dto.id}/$view"
        window.history.pushState("", "", path)
        state = NavState(path, "")
        window.dispatchEvent(Event(EVENT))
    }

    fun changeLocation(view: String, fetch: suspend () -> RecordDto<*>) =
        launch { changeLocation(fetch(), view) }

    fun changeLocation(path: String) {
        window.history.pushState("", "", path)
        state = NavState(path, "")
        window.dispatchEvent(Event(EVENT))
    }

    fun changeView(view: String) {

    }

    fun stepBack() {

    }

    fun stepForward() {

    }

}