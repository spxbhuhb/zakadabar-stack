/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

import kotlinx.browser.window
import org.w3c.dom.events.Event
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.launch

object Navigation {

    const val CREATE = "create"
    const val READ = "read"
    const val UPDATE = "update"
    const val DELETE = "delete"
    const val ALL = "all"

    const val EVENT = "z-navigation"

    var state = NavigationState(NavigationState.StateType.Page)

    fun init() {
        window.addEventListener("popstate", onPopState)
        decodeLocation(window.location.pathname, window.location.search)
        window.dispatchEvent(Event(EVENT))
    }

    private val onPopState = fun(_: Event) {
        decodeLocation(window.location.pathname, window.location.search)
        window.dispatchEvent(Event(EVENT))
    }

    fun decodeLocation(path: String, query: String) {
        val trimmedPath = path.trim('/')
        state = when {
            trimmedPath.isEmpty() -> NavigationState(NavigationState.StateType.Home)
            trimmedPath.startsWith("view/") -> decodeViewURL(trimmedPath, query)
            trimmedPath.startsWith("page/") -> decodePageURL(trimmedPath, query)
            else -> NavigationState(NavigationState.StateType.Unknown)
        }
    }

    private fun decodeViewURL(path: String, queryData: String): NavigationState {
        val segments = path.trim('/').split("/")
        var index = 1 // start with 1 to skip "/view/"

        val pathItems = mutableListOf<NavigationState.PathItem>()

        while (segments.size - index > 4) {
            val moduleShid = segments[index ++]
            val localName = segments[index ++]
            val localId = segments[index ++].toLong()
            pathItems += NavigationState.PathItem("$moduleShid/$localName", localId)
        }

        val moduleShid = segments[index ++]
        val localName = segments[index ++]
        val dataType = "$moduleShid/$localName"

        val localId = if (index == segments.size - 1) null else segments[index ++].toLong()

        val viewName = segments[index]

        val query = if (window.location.search.isNotEmpty()) {
            val dtoFrontend = FrontendContext.dtoFrontends[dataType] ?: throw IllegalStateException("missing dto frontend for $dataType")
            dtoFrontend.decodeQuery(viewName, queryData)
        } else {
            Any()
        }

        return NavigationState(
            NavigationState.StateType.View,
            viewState = NavigationState.ViewState(
                pathItems,
                dataType,
                localId,
                viewName,
                query
            )
        )
    }

    private fun decodePageURL(path: String, queryData: String): NavigationState {
        return NavigationState(
            NavigationState.StateType.Page,
            pageState = NavigationState.PageState(
                path.substringAfter('/'),
                queryData
            )
        )
    }

    fun changeLocation(type: String, id: Long, view: String) {

    }

    fun changeLocation(dto: RecordDto<*>, view: String) {
        val path = "/view/${dto.getType()}/${dto.id}/$view"
        window.history.pushState("", "", path)
        decodeLocation(path, "")
        window.dispatchEvent(Event(EVENT))
    }

    fun changeLocation(view: String, fetch: suspend () -> RecordDto<*>) =
        launch { changeLocation(fetch(), view) }

    fun changeLocation(path: String) {
        window.history.pushState("", "", path)
        decodeLocation(path, "")
        window.dispatchEvent(Event(EVENT))
    }

    fun changeView(view: String) {

    }

    fun stepInto(type: String, id: Long, view: String) {

    }

    fun stepOut() {

    }

    fun stepBack() {

    }

    fun stepForward() {

    }

}