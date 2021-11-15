/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.application

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.core.browser.dock.ZkDock
import zakadabar.core.browser.modal.ZkConfirmDialog
import zakadabar.core.browser.modal.ZkModalContainer
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.toast.ZkToastContainer
import zakadabar.core.browser.util.decodeURIComponent
import zakadabar.core.browser.util.io
import zakadabar.core.module.modules
import zakadabar.core.resource.*
import zakadabar.core.server.ServerDescriptionBo
import zakadabar.core.text.TranslationProvider
import zakadabar.core.text.capitalized

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
@Deprecated(
    "EOL: 2021.8.1 import stringStore from `zakadabar.stack.resources`", ReplaceWith(
        "localizedStrings", "zakadabar.stack.resources.localizedStrings"
    )
)
val stringStore
    get() = localizedStrings

@Deprecated(
    "EOL: 2021.8.1 use `localized` from `zakadabar.stack.resources`", ReplaceWith(
        "localized<T>()", "zakadabar.stack.resources.localized"
    )
)
inline fun <reified T> translate() = localizedStrings.getNormalized(T::class.simpleName !!)

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

    lateinit var sessionManager: ZkSessionManager

    lateinit var locale: String

    lateinit var executor: ZkExecutor

    lateinit var serverDescription: ServerDescriptionBo

    lateinit var routing: ZkAppRouting

    @Deprecated("use global modules instead")
    var services = modules

    val stringStores = mutableListOf<ZkStringStore>()

    @Deprecated("EOL: 2021.8.1  -  import stringStore from `zakadabar.stack.resources`")
    lateinit var stringStore: ZkBuiltinStrings

    lateinit var dock: ZkDock

    lateinit var toasts: ZkToastContainer

    lateinit var modals: ZkModalContainer

    lateinit var popup: HTMLElement

    var _title: ZkAppTitle? = null

    var title: ZkAppTitle
        get() = _title !!
        set(value) {
            onTitleChange?.invoke(value)
            _title = value
        }

    var onTitleChange: ((newTitle: ZkAppTitle) -> Unit)? = null

    val navStateChangeEvent = "zk-navstate-change"

    var pendingModificationsEnabled = false

    var pendingModifications = false

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

        popup = document.createElement("div") as HTMLElement
        document.body?.appendChild(popup)

        modules.resolveDependencies()
        modules.start()

        window.addEventListener("popstate", onPopState)

        val current = window.history.state?.toString() ?: ""
        if (current.isEmpty()) {
            window.history.replaceState(incrementNavCounter(), "")
        } else {
            window.sessionStorage["zk-nav-last-shown"] = current
        }

        val path = decodeURIComponent(window.location.pathname)
        routing.onNavStateChange(ZkNavState(path, window.location.search, window.location.hash, new = true))
        window.dispatchEvent(Event(navStateChangeEvent))
    }

    suspend fun initSession() {
        sessionManager = modules.firstOrNull() ?: EmptySessionManager()
        sessionManager.init()
    }

    fun initRouting(routing: ZkAppRouting) {
        this.routing = routing
        routing.init()
    }

    suspend fun initLocale(store: ZkBuiltinStrings, defaultLocale: String? = null) {
        val path = decodeURIComponent(window.location.pathname).trim('/')

        locale = when {
            path.isNotEmpty() -> path.substringBefore('/')
            ::executor.isInitialized && executor.account.locale.isNotBlank() -> executor.account.locale
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

        modules.firstOrNull<TranslationProvider>()?.translate(store, locale)

        localizedStrings = store
        localizedFormats = BuiltinLocalizedFormats()
    }

    /**
     * Called when:
     *  - the user clicks on the "Back" button
     *  - the user clicks on the "Forward" button
     *  - the [back] function is called
     *  - direct call to window.history.back
     *  - direct call to window.history.forward
     */
    val onPopState = fun(_: Event) {
        io {
            if (pendingModificationsEnabled && pendingModifications) {
                if (! ZkConfirmDialog(localizedStrings.confirmation.capitalized(), localizedStrings.notSaved).run()) {
                    window.history.pushState(null, "", null)
                    return@io
                } else {
                    pendingModifications = false
                }
            }

            val current = (window.history.state as? Int) ?: 0
            val last = window.sessionStorage["zk-nav-last-shown"]?.toInt() ?: 0
            window.sessionStorage["zk-nav-last-shown"] = current.toString()

            val backward = last > current

            val path = decodeURIComponent(window.location.pathname)

            routing.onNavStateChange(
                ZkNavState(
                    path,
                    window.location.search,
                    window.location.hash,
                    forward = ! backward,
                    backward = backward
                )
            )

            window.dispatchEvent(Event(navStateChangeEvent))
        }
    }

    protected fun incrementNavCounter(): Int {
        val navCounter = (window.sessionStorage["zk-nav-counter"]?.toInt() ?: window.history.length) + 1
        window.sessionStorage["zk-nav-counter"] = navCounter.toString()
        window.sessionStorage["zk-nav-last-shown"] = navCounter.toString()
        return navCounter
    }

    fun changeNavState(path: String, query: String = "", hash: String = "") {
        io {
            if (pendingModificationsEnabled && pendingModifications) {
                if ( ! ZkConfirmDialog(localizedStrings.confirmation.capitalized(), localizedStrings.notSaved).run()) {
                    return@io
                }
            }

            application.pendingModifications = false

            var url = if (hash.isEmpty()) path else "$path#$hash"
            url = if (query.isEmpty()) url else "$url?$query"

            window.history.pushState(incrementNavCounter(), "", url)

            routing.onNavStateChange(ZkNavState(path, query, hash, new = true))
            window.dispatchEvent(Event(navStateChangeEvent))
        }
    }

    fun changeNavState(target: ZkAppRouting.ZkTarget, path: String? = null, query: String = "") {
        val url = when {
            path == null -> routing.toLocalUrl(target)
            path.startsWith('/') -> routing.toLocalUrl(target) + path
            else -> routing.toLocalUrl(target) + '/' + path
        }
        changeNavState(url, query)
    }

    fun replaceNavState(path: String = routing.navState.urlPath, query: String = routing.navState.urlQuery, hash: String = routing.navState.urlHashtag) {
        var url = if (hash.isEmpty()) path else "$path#$hash"
        url = if (query.isEmpty()) url else "$url?$query"
        window.history.replaceState(window.history.state, "", url)
    }

    fun forward() = window.history.forward()

    fun back() = window.history.back()

}
