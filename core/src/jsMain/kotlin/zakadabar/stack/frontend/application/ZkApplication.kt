/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import zakadabar.stack.frontend.application.ZkApplication.dock
import zakadabar.stack.frontend.application.ZkApplication.executor
import zakadabar.stack.frontend.application.ZkApplication.modals
import zakadabar.stack.frontend.application.ZkApplication.onTitleChange
import zakadabar.stack.frontend.application.ZkApplication.routing
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.application.ZkApplication.themes
import zakadabar.stack.frontend.application.ZkApplication.title
import zakadabar.stack.frontend.application.ZkApplication.toasts
import zakadabar.stack.frontend.builtin.dock.ZkDock
import zakadabar.stack.frontend.builtin.modal.ZkModalContainer
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.toast.ZkToastContainer
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.resources.ZkBuiltinStrings
import zakadabar.stack.util.PublicApi

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
 * @property  themes     List of known UI themes.
 *
 * @property  theme      The design theme of the application.
 *
 * @property  strings    The string store that contains the strings the application uses.
 *
 * @property  dock       A container to show sub-windows such as mail editing in Gmail.
 *
 * @property  toasts     A container to show toasts.
 *
 * @property  modals     A container that contains modal windows.
 *
 * @property  title      Current title of the page.
 *
 * @property  onTitleChange  Called when [title] changes, layouts replace this function.
 *
 */
object ZkApplication {

    var sessionManager = ZkSessionManager()

    lateinit var executor: ZkExecutor

    lateinit var routing: ZkAppRouting

    val themes = mutableListOf<ZkTheme>()

    var theme: ZkTheme = ZkBuiltinLightTheme()
        set(value) {
            field = value
            window.localStorage.setItem(THEME_STORAGE_KEY, value.name)
            applyThemeToBody()
            styleSheets.forEach { it.onThemeChange(value) }
        }

    val styleSheets = mutableListOf<ZkCssStyleSheet<*>>()

    lateinit var strings: ZkBuiltinStrings

    lateinit var dock: ZkDock

    lateinit var toasts: ZkToastContainer

    lateinit var modals: ZkModalContainer

    var title = ZkAppTitle("")
        set(value) {
            onTitleChange?.invoke(value)
            field = title
        }

    var onTitleChange: ((newTitle: ZkAppTitle) -> Unit)? = null

    @Suppress("MemberVisibilityCanBePrivate")
    const val NAVSTATE_CHANGE = "zk-navstate-change"

    private const val THEME_STORAGE_KEY = "zk-theme-name"

    fun init() {

        applyThemeToBody()

        dock = ZkDock().apply {
            onCreate()
            onResume()
        }

        toasts = ZkToastContainer().apply {
            onCreate()
            onResume()
        }

        modals = ZkModalContainer().apply {
            onCreate()
            onResume()
        }

        window.addEventListener("popstate", onPopState)
        routing.onNavStateChange(ZkNavState(window.location.pathname, window.location.search))
        window.dispatchEvent(Event(NAVSTATE_CHANGE))
    }

    fun initTheme(): ZkTheme {
        val themeName = (executor.account.theme ?: window.localStorage.getItem(THEME_STORAGE_KEY)) ?: "default-light"
        return themes.firstOrNull { it.name == themeName } ?: if (themes.isEmpty()) ZkBuiltinLightTheme() else themes.first()
    }

    private fun applyThemeToBody() {
        with(document.body?.style !!) {
            fontFamily = theme.fontFamily
            fontSize = theme.fontSize
            fontWeight = theme.fontWeight
            backgroundColor = theme.layout.defaultBackground
            color = theme.layout.defaultForeground
        }
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

    /**
     * Translates the given string if there is a translation in the application's
     * string store. Returns with the original string when there is no translation.
     */
    fun t(original: String) = strings.map[original] ?: original

    @PublicApi
    fun hasRole(roleName: String) = roleName in executor.roles
}
