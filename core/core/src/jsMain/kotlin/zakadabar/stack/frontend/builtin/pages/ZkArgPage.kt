/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.frontend.application.ZkAppLayout
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkNavState
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.util.encodeURIComponent
import zakadabar.stack.frontend.util.log
import zakadabar.stack.frontend.util.newInstance

/**
 * Base class for pages that receive parameters in the URL, such as queries
 * or pages with state that can be opened directly with the URL.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkArgPage<T>() : ZkPage() {

    lateinit var serializer: KSerializer<T>

    /**
     * The arguments passed to this page in the URL. Null, when the arguments cannot
     * be loaded or are not in the URL.
     */
    var argsOrNull : T? = null

    /**
     * The arguments passed to this page in the URL. When the arguments cannot
     * be loaded or are not in the URL access throws an exception.
     */
    val args: T get() = argsOrNull!!

    /**
     * When true (default) URL decoding exceptions at caught and are treated
     * as there would be no arguments in the URL. When false, the exceptions
     * go though.
     */
    open val hideUrlError = true

    constructor(
        serializer : KSerializer<T>,
        layout: ZkAppLayout? = null,
        css: ZkCssStyleRule? = null
    ) : this() {
        init(serializer, layout, css)
    }

    fun init(
        serializer: KSerializer<T>,
        layout: ZkAppLayout? = null,
        css: ZkCssStyleRule? = null
    ) {
        this.serializer = serializer
        init(layout, css)
    }

    override fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement {
        val argsOrNull = try {
            state.args?.let { Json.decodeFromString(serializer, it) }
        } catch (ex: Exception) {
            if (hideUrlError) {
                log(ex)
                null
            } else {
                throw ex
            }
        }

        return if (factory) {
            this::class.newInstance().also {
                it.init(serializer, layout, css)
                it.argsOrNull = argsOrNull
            }
        } else {
            this
        }
    }

    override fun onResume() {
        super.onResume()
        setAppTitleBar()
    }

    open fun open(args: T) {
        val a = encodeURIComponent(Json.encodeToString(serializer, args))
        application.changeNavState(this, "", "args=$a")
    }

}