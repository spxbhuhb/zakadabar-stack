/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.stack.Stack
import zakadabar.stack.comm.websocket.session.StackClientSession
import zakadabar.stack.data.builtin.security.CommonAccountDto
import zakadabar.stack.frontend.FrontendContext.dispatcher
import zakadabar.stack.frontend.FrontendContext.dtoFrontends
import zakadabar.stack.frontend.FrontendContext.stackSession
import zakadabar.stack.frontend.builtin.dock.Dock
import zakadabar.stack.frontend.comm.rest.FrontendComm
import zakadabar.stack.frontend.extend.DtoFrontend
import zakadabar.stack.frontend.extend.FrontendModule
import zakadabar.stack.frontend.util.Dictionary
import zakadabar.stack.frontend.util.defaultTheme
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.messaging.MessageDispatcher
import zakadabar.stack.messaging.SyncMessageDispatcher
import zakadabar.stack.util.UUID
import zakadabar.stack.util.Unique

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
 * @property  dtoFrontends    A map of dto type (such as "3a8627/folder") and
 *                            [DtoFrontend] pairs. Modules add their
 *                            entity types into this map.
 *
 * @property  stackSession    The websocket session used to communicate with the
 *                            backend side of the stack. Lazy initialization.
 */
object FrontendContext {

    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var executor: CommonAccountDto

    private var defaultLanguage = window.navigator.language

    var theme = defaultTheme

    private val dictionaries = mutableMapOf<UUID, Dictionary>()

    val dispatcher: MessageDispatcher = SyncMessageDispatcher()

    val dtoFrontends = mutableMapOf<String, DtoFrontend<*>>()

    lateinit var dock: Dock

    // ----  Init ----------------------------------------------------------

    suspend fun init() {
        document.body?.style?.fontFamily = theme.fontFamily

        val id = window.fetch("/api/${Stack.shid}/who-am-i").await()
            .text().await()
            .toLong()

        CommonAccountDto.comm = FrontendComm(CommonAccountDto.type, CommonAccountDto.serializer())

        executor = CommonAccountDto.comm.read(id)

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

    // ----  DTO Frontends  -------------------------------------------------

    operator fun plusAssign(support: DtoFrontend<*>) {
        dtoFrontends[support.type] = support
    }

}