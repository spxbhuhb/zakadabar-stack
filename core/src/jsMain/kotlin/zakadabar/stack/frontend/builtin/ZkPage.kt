/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin

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
        fun launchBuildNewPage(viewName: String, builder: suspend ZkElement.() -> Unit): ZkPage {
            val p = ZkPage()
            p.viewName = viewName
            return p.launchBuild(builder) as ZkPage
        }

        /**
         * Creates an anonymous [ZkPage] and calls [ZkElement.build] on it with
         * the [builder] function.
         *
         * Use this function when there is no need to asynchronous data fetch.
         */
        fun buildNewPage(viewName: String, builder: ZkElement.() -> Unit): ZkPage {
            val p = ZkPage()
            p.viewName = viewName
            return p.build(builder) as ZkPage
        }
    }

    override var viewName = "${this::class.simpleName}"

    open fun open() = Application.changeNavState("/$viewName")

    override fun route(routing: AppRouting, state: NavState): ZkElement {
        if (layout != null) routing.nextLayout = layout
        return this
    }

}