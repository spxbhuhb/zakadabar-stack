/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import zakadabar.stack.frontend.application.ZkApplication.dock
import zakadabar.stack.frontend.application.ZkApplication.executor
import zakadabar.stack.frontend.application.ZkApplication.routing
import zakadabar.stack.frontend.application.ZkApplication.stringStore
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.application.ZkApplication.toasts
import zakadabar.stack.frontend.builtin.dock.ZkDock
import zakadabar.stack.frontend.builtin.toast.ZkToastContainer
import zakadabar.stack.frontend.resources.ZkStringStore
import zakadabar.stack.frontend.resources.ZkTheme

/**
 * The application that runs in the browser window. This object contains data
 * and resources that are used all over the application.
 *
 * It listens to the location change events of the browser window and loads
 * the pages that belongs to the current URL.
 *
 * @property  executor   The user who views the web page. There is always a user,
 *                       before login it is "anonymous".
 *
 * @property  routing    The URL -> page mapping to navigate in the application.
 *
 * @property  theme      The design theme of the application.
 *
 * @property  stringStore    The string store that contains the strings the application uses.
 *
 * @property  dock       A container to show sub-windows such as mail editing in Gmail.
 *
 * @property  toasts     A container to show toasts.
 */
object ZkApplication {

    lateinit var executor: ZkExecutor

    lateinit var routing: ZkAppRouting

    lateinit var theme: ZkTheme

    lateinit var stringStore: ZkStringStore

    lateinit var dock: ZkDock

    lateinit var toasts: ZkToastContainer

    @Suppress("MemberVisibilityCanBePrivate")
    const val NAVSTATE_CHANGE = "zk-navstate-change"

    fun init() {
        with(document.body?.style !!) {
            fontFamily = theme.font.family
            fontSize = theme.font.size
            fontWeight = theme.font.weight
        }

        dock = ZkDock().also {
            it.onCreate()
            it.onResume()
        }

        toasts = ZkToastContainer().also {
            it.onCreate()
            it.onResume()
        }

        window.addEventListener("popstate", onPopState)
        routing.onNavStateChange(ZkNavState(window.location.pathname, window.location.search))
        window.dispatchEvent(Event(NAVSTATE_CHANGE))
    }

    private val onPopState = fun(_: Event) {
        routing.onNavStateChange(ZkNavState(window.location.pathname, window.location.search))
        window.dispatchEvent(Event(NAVSTATE_CHANGE))
    }

    fun changeNavState(path: String, query: String = "") {
        val url = if (query.isEmpty()) path else "$path?$query"
        window.history.pushState("", "", url)
        routing.onNavStateChange(ZkNavState(path, query))
        window.dispatchEvent(Event(NAVSTATE_CHANGE))
    }

    fun back() = window.history.back()

}
