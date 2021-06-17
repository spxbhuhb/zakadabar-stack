/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.frontend.builtin.dock.ZkDock
import zakadabar.stack.frontend.builtin.modal.ZkModalContainer
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.toast.ZkToastContainer
import zakadabar.stack.resources.ZkBuiltinStrings
import zakadabar.stack.resources.ZkStringStore
import zakadabar.stack.resources.localizedStrings
import zakadabar.stack.text.TranslationProvider
import zakadabar.stack.util.InstanceStore

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
@Deprecated("EOL: 2021.8.1 import stringStore from `zakadabar.stack.resources`", ReplaceWith(
    "localizedStrings", "import zakadabar.stack.resources.localizedStrings"
))
val stringStore
    get() = localizedStrings

@Deprecated("EOL: 2021.8.1 use `localized` from `zakadabar.stack.resources`", ReplaceWith(
    "localized<T>()", "import zakadabar.stack.resources.localized"
))
inline fun <reified T> translate() = localizedStrings.getNormalized(T::class.simpleName!!)

/**
 * Find a routing target.
 *
 * @param T Class or interface of the target to find.
 */
inline fun <reified T : ZkAppRouting.ZkTarget> target() = application.routing.first(T::class)

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
 * @property  stringStore    The string store that contains the strings the application uses.
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

    lateinit var sessionManager : ZkSessionManager

    lateinit var locale: String

    lateinit var executor: ZkExecutor

    lateinit var serverDescription: ServerDescriptionBo

    lateinit var routing: ZkAppRouting

    var services = InstanceStore<Any>()

    val stringStores = mutableListOf<ZkStringStore>()

    @Deprecated("EOL: 2021.8.1 import stringStore from `zakadabar.stack.resources`")
    lateinit var stringStore: ZkBuiltinStrings

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
        routing.onNavStateChange(ZkNavState(window.location.pathname, window.location.search, window.location.hash))
        window.dispatchEvent(Event(navStateChangeEvent))
    }

    suspend fun initSession() {
        sessionManager = services.firstOrNull() ?: EmptySessionManager()
        sessionManager.init()
    }

    fun initRouting(routing: ZkAppRouting) {
        this.routing = routing
        routing.init()
    }

    suspend fun initLocale(store: ZkBuiltinStrings, defaultLocale : String? = null) {
        val path = window.location.pathname.trim('/')

        locale = when {
            path.isNotEmpty() -> path.substringBefore('/')
            ::executor.isInitialized && executor.account.locale.isNotBlank()-> executor.account.locale
            ::serverDescription.isInitialized && serverDescription.defaultLocale.isNotBlank() -> serverDescription.defaultLocale
            else -> defaultLocale ?: ""
        }

        if (locale.isBlank()) {
            document.body?.innerText = "Could not initialize locale."
            throw IllegalStateException()
        }

        stringStores.forEach {
            store += it
        }

        services.firstOrNull<TranslationProvider>()?.translate(store, locale)

        localizedStrings = store
    }

    private val onPopState = fun(_: Event) {
        routing.onNavStateChange(ZkNavState(window.location.pathname, window.location.search, window.location.hash))
        window.dispatchEvent(Event(navStateChangeEvent))
    }

    fun changeNavState(path: String, query: String = "") {
        val url = if (query.isEmpty()) path else "$path?$query"
        window.history.pushState("", "", url)
        routing.onNavStateChange(ZkNavState(path, query))
        window.dispatchEvent(Event(navStateChangeEvent))
    }

    fun replaceNavState(path: String = routing.navState.urlPath, query: String = routing.navState.urlQuery, hash: String = routing.navState.urlHashtag) {
        var url = if (hash.isEmpty()) path else "$path#$hash"
        url = if (query.isEmpty()) url else "$url?$query"
        window.history.replaceState("", "", url)
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
