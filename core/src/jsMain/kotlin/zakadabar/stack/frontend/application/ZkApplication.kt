/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import zakadabar.stack.data.builtin.misc.ServerDescriptionDto
import zakadabar.stack.data.builtin.resources.TranslationsByLocale
import zakadabar.stack.frontend.builtin.dock.ZkDock
import zakadabar.stack.frontend.builtin.modal.ZkModalContainer
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.toast.ZkToastContainer
import zakadabar.stack.resources.ZkBuiltinStrings
import zakadabar.stack.resources.ZkStringStore

/**
 * The application itself, initialized in main.kt.
 */
lateinit var application: ZkApplication

/**
 * Shorthand for application.executor
 */
val executor
    get() = application.executor

/**
 * Global string store that contains the translated strings to show to the user.
 * This is usually not a ZkBuiltinStrings in itself but an extension of it.
 */
val stringStore
    get() = application.strings

/**
 * Check if the executor has the given role.
 */
fun hasRole(roleName: String) = roleName in application.executor.roles

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
open class ZkApplication {

    open var sessionManager = ZkSessionManager()

    lateinit var locale: String

    lateinit var executor: ZkExecutor

    lateinit var serverDescription : ServerDescriptionDto

    lateinit var routing: ZkAppRouting

    lateinit var strings: ZkBuiltinStrings

    lateinit var dock: ZkDock

    lateinit var toasts: ZkToastContainer

    lateinit var modals: ZkModalContainer

    private var _title: ZkAppTitle? = null

    var title: ZkAppTitle
        get() = _title !!
        set(value) {
            onTitleChange?.invoke(value)
            _title = value
        }

    var onTitleChange: ((newTitle: ZkAppTitle) -> Unit)? = null

    @Suppress("MemberVisibilityCanBePrivate")
    val navStateChangeEvent = "zk-navstate-change"

    fun run() {
        title = ZkAppTitle("")

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
        window.dispatchEvent(Event(navStateChangeEvent))
    }

    suspend fun initSession() {
        sessionManager.init()
    }

    fun initRouting(routing : ZkAppRouting) {
        this.routing = routing
        routing.init()
    }

    suspend fun initLocale(store : ZkBuiltinStrings, downloadTranslations : Boolean = true) {
        val path = window.location.pathname.trim('/')
        locale = when {
            path.isNotEmpty() -> path.substringBefore('/')
            ::executor.isInitialized -> executor.account.locale
            else -> {
                document.body?.innerText = "Could not initialize locale."
                throw IllegalStateException()
            }
        }
        strings = if (downloadTranslations) {
            store.merge(TranslationsByLocale(locale).execute())
        } else {
            store
        }
    }

    private val onPopState = fun(_: Event) {
        routing.onNavStateChange(ZkNavState(window.location.pathname, window.location.search))
        window.dispatchEvent(Event(navStateChangeEvent))
    }

    fun changeNavState(path: String, query: String = "") {
        val url = if (query.isEmpty()) path else "$path?$query"
        window.history.pushState("", "", url)
        routing.onNavStateChange(ZkNavState(path, query))
        window.dispatchEvent(Event(navStateChangeEvent))
    }

    fun changeNavState(target: ZkAppRouting.ZkTarget, path: String? = null, query: String = "") {
        val url = when {
            path == null -> routing.toLocalUrl(target)
            path.startsWith('/') -> routing.toLocalUrl(target) + path
            else -> routing.toLocalUrl(target) + '/' + path
        }
        changeNavState(url, query)
    }

    fun back() = window.history.back()

}
