/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import zakadabar.stack.Stack
import zakadabar.stack.frontend.builtin.dock.Dock
import zakadabar.stack.frontend.data.DtoFrontend
import zakadabar.stack.frontend.util.Dictionary
import zakadabar.stack.frontend.util.defaultTheme
import zakadabar.stack.util.UUID
import zakadabar.stack.util.Unique

/**
 * The application that runs in the browser window. This object contains data
 * and resources that are used all over the application.
 */
object Application {

    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var executor: Executor

    lateinit var routing: AppRouting

    private var defaultLanguage = window.navigator.language

    var theme = defaultTheme

    private val dictionaries = mutableMapOf<UUID, Dictionary>()

    val dtoFrontends = mutableMapOf<String, DtoFrontend<*>>()

    lateinit var dock: Dock

    const val EVENT = "zk-navstate-change"

    fun init() {
        document.body?.style?.fontFamily = theme.fontFamily
        dock = Dock().init() // this does not add it to the DOM, it's just created

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

    fun back() = window.history.back()

    fun t(text: String) =
        dictionaries[Stack.uuid]?.get(defaultLanguage)?.get(text) ?: text

    fun t(text: String, namespace: UUID) =
        dictionaries[namespace]?.get(defaultLanguage)?.get(text) ?: text

    fun t(text: String, namespace: Unique) =
        dictionaries[namespace.uuid]?.get(defaultLanguage)?.get(text) ?: text

    fun t(text: String, namespace: UUID, language: String) =
        dictionaries[namespace]?.get(language)?.get(text) ?: text

    fun t(text: String, namespace: Unique, language: String) =
        dictionaries[namespace.uuid]?.get(language)?.get(text) ?: text
}