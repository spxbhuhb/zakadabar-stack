/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.NavState

/**
 * Provides common functions used in most page implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkPage(
    val layout: AppLayout? = null
) : ZkElement(), AppRouting.ZkTarget {

    companion object {
        /**
         * Creates an anonymous [ZkPage] and calls [ZkElement.launchBuild] on it with
         * the [builder] function.
         *
         * Use this function when you need to fetch data asynchronously during building
         * the element.
         */
        fun launchBuildNewPage(viewPrefix: String, module: String? = null, builder: suspend ZkElement.() -> Unit): ZkPage {
            val p = ZkPage()
            if (module != null) p.module = module
            p.viewPrefix = viewPrefix
            return p.launchBuild(builder) as ZkPage
        }

        /**
         * Creates an anonymous [ZkPage] and calls [ZkElement.build] on it with
         * the [builder] function.
         *
         * Use this function when there is no need to asynchronous data fetch.
         */
        fun buildNewPage(viewPrefix: String, module: String? = null, builder: ZkElement.() -> Unit): ZkPage {
            val p = ZkPage()
            if (module != null) p.module = module
            p.viewPrefix = viewPrefix
            return p.build(builder) as ZkPage
        }
    }

    private var _module = "default"

    override var module
        get() = _module
        set(value) {
            _module = value
        }

    private var _viewPrefix = "/${this::class.simpleName}"

    override var viewPrefix
        get() = _viewPrefix
        set(value) {
            _viewPrefix = value
        }

    val url
        get() = "/$_module$_viewPrefix"

    open fun open() = Application.changeNavState(url)

    override fun route(routing: AppRouting, state: NavState): ZkElement {
        if (layout != null) routing.nextLayout = layout
        return this
    }

}