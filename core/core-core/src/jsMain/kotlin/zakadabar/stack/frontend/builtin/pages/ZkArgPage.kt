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
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleProvider
import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.util.encodeURIComponent
import zakadabar.stack.frontend.util.log
import zakadabar.stack.frontend.util.plusAssign

/**
 * Provides common functions used in most page implementations which
 * receive arguments in the URL.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkArgPage<T>(
    val serializer: KSerializer<T>,
    val layout: ZkAppLayout? = null,
    cssClass: ZkCssStyleRule? = null
) : ZkElement(), ZkAppRouting.ZkTarget, ZkAppTitleProvider {

    var args: T? = null

    override var viewName = "${this::class.simpleName}"

    override var setAppTitle = true
    override var titleText: String? = null
    override var titleElement: ZkAppTitle? = null

    open fun open(args: T) {
        val a = encodeURIComponent(Json.encodeToString(serializer, args))
        application.changeNavState(this, "args=$a")
    }

    override fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement {
        args = try {
            state.args?.let { Json.decodeFromString(serializer, it) }
        } catch (ex: Exception) {
            log(ex)
            null
        }

        if (layout != null) routing.nextLayout = layout

        return this
    }

    init {
        classList += cssClass ?: zkPageStyles.scrollable
    }

    override fun onResume() {
        super.onResume()
        setAppTitleBar()
    }

}