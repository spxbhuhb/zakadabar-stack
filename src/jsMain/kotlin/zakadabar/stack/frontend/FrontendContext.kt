/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.frontend

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.stack.Stack
import zakadabar.stack.comm.session.StackClientSession
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.frontend.FrontendContext.dispatcher
import zakadabar.stack.frontend.FrontendContext.entitySupports
import zakadabar.stack.frontend.FrontendContext.scopedViews
import zakadabar.stack.frontend.FrontendContext.stackSession
import zakadabar.stack.frontend.FrontendContext.views
import zakadabar.stack.frontend.builtin.dock.Dock
import zakadabar.stack.frontend.comm.rest.CachedRestComm
import zakadabar.stack.frontend.extend.*
import zakadabar.stack.frontend.util.Dictionary
import zakadabar.stack.frontend.util.defaultTheme
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.messaging.MessageDispatcher
import zakadabar.stack.messaging.SyncMessageDispatcher
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID
import zakadabar.stack.util.Unique
import kotlin.reflect.KClass

/**
 * Access points for the most common frontend data structures and utilities.
 * One frontend (one browser tab for example) has one context. To use a
 * different context another tab or an iframe is needed.
 *
 * Switching language and the styles require refreshing of the page in the
 * browser. That is fine, as the user does these rarely and most of the page
 * has to be changed anyway.
 *
 * @property  dispatcher      Dispatcher to send messages to other components.
 *
 * @property  views           Entity independent views added by modules. See
 *                            [ViewContract] for more information.
 *
 * @property  scopedViews     Entity independent scoped views added by modules.
 *                            See [ScopedViewContract] for more information.
 *
 * @property  entitySupports  A map of entity type (such as "3a8627/folder") and
 *                            [FrontendEntitySupport] pairs. Modules add their
 *                            entity types into this map.
 *
 * @property  stackSession    The websocket session used to communicate with the
 *                            backend side of the stack. Lazy initialization.
 */
object FrontendContext {

    lateinit var executor: CommonAccountDto

    private var defaultLanguage = window.navigator.language

    var theme = defaultTheme

    private val dictionaries = mutableMapOf<UUID, Dictionary>()

    val dispatcher: MessageDispatcher = SyncMessageDispatcher()

    val entitySupports = mutableMapOf<String, FrontendEntitySupport<*>>()

    private val views = ViewCatalog()

    private val scopedViews = mutableMapOf<Any, ViewCatalog>()

    lateinit var dock: Dock

    // ----  Init ----------------------------------------------------------

    suspend fun init() {
        document.body?.style?.fontFamily = theme.fontFamily

        val id = window.fetch("/api/${Stack.shid}/who-am-i").await()
            .text().await()
            .toLong()

        CommonAccountDto.comm = CachedRestComm(CommonAccountDto.type, CommonAccountDto.serializer())

        executor = CommonAccountDto.comm.get(id)

        dock = Dock().init() // this does not add it to the DOM, it's just created
    }

    // ----  Comm ----------------------------------------------------------

    /**
     * Transfer session used for file uploads. We don't have to open it until
     * the user actually tries to upload a file, hence lazy.
     */
    val stackSession by lazy {

        val session = StackClientSession(
            host = window.location.host.substringBefore(':'),
            path = "/api/${Stack.shid}/ws",
            port = 8080 // FIXME window.location.port.toInt()
        )

        launch { session.open() }

        session
    }

    // ----  Modules -------------------------------------------------

    operator fun plusAssign(module: FrontendModule) {
        module.init()
    }

    // ----  I18n ----------------------------------------------------------

    operator fun plusAssign(pair: Pair<UUID, Dictionary>) {
        dictionaries[pair.first] = pair.second
    }

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

    // ----  Entity Types  -------------------------------------------------

    operator fun plusAssign(support: FrontendEntitySupport<*>) {
        entitySupports[support.type] = support
    }

    // ----  Views  --------------------------------------------------------

    operator fun plusAssign(view: ViewContract) {
        view.installOrder = views.size
        views += view
    }

    operator fun plusAssign(scopeAndView: Pair<Any, ScopedViewContract>) {
        val (scope, view) = scopeAndView

        if (! scopedViews.containsKey(scope)) scopedViews[scope] = ViewCatalog()

        val scopeViews = scopedViews[scope] !!

        view.installOrder = scopeViews.size
        scopeViews += view
    }

    operator fun minusAssign(scope: Any) {
        scopedViews.remove(scope)
    }

    /**
     * Gets an instance of the first top priority view for the given target.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> newInstance(
        target: UUID,
        clazz: KClass<T>,
        scope: Any? = null,
        test: ((ViewContract) -> Boolean)? = null
    ): T? {

        val catalog = if (scope == null) views else scopedViews[scope] ?: views
        val contract = catalog.getTop(target, test) ?: return null

        val instance = if (contract is ScopedViewContract) {
            contract.newInstance(scope)
        } else {
            contract.newInstance()
        }

        return if (clazz.isInstance(instance)) instance as T else null
    }

    /**
     * Get instances for all top priority views for the given slot. If there
     * are views with the same uuid and same priority all of them are included.
     */
    @PublicApi
    fun <T : Any> allNewInstances(
        target: UUID,
        clazz: KClass<T>,
        scope: Any? = null,
        test: ((ViewContract) -> Boolean)? = null
    ): List<T> {

        val tops = allContracts(target, scope, test)

        return tops.mapNotNull { contract ->

            val instance = if (contract is ScopedViewContract) {
                contract.newInstance(scope)
            } else {
                contract.newInstance()
            }

            @Suppress("UNCHECKED_CAST") // this is fine as we check the class just before
            if (clazz.isInstance(instance)) instance as T else null
        }
    }

    /**
     * Get all top priority contracts for the given slot. If there
     * are contracts with the same uuid and same priority all of them are included.
     */
    @PublicApi
    fun allContracts(target: UUID, scope: Any? = null, test: ((ViewContract) -> Boolean)? = null): List<ViewContract> {

        val tops = mutableListOf<ViewContract>()

        val catalog = if (scope == null) views else scopedViews[scope] ?: views

        catalog.getAll(target, test).forEach { candidate ->

            var found = false

            for (i in tops.indices) {
                if (tops[i].uuid != candidate.uuid) continue
                if (tops[i].priority > candidate.priority) continue

                found = true

                if (tops[i].priority < candidate.priority) {
                    tops[i] = candidate
                } else {
                    tops += candidate
                }

                break
            }

            if (! found) tops += candidate
        }

        // two sorts mean that the second has preference, but in the same position install order matters
        tops.sortBy { it.installOrder }
        tops.sortBy { it.position }

        return tops
    }
}