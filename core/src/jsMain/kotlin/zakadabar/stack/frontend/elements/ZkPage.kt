/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.NavState

/**
 * Provides common functions used in most page implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkPage(
    final override val module: String,
    final override val viewPrefix: String
) : ZkElement(), AppRouting.ZkTarget {

    companion object {
        /**
         * Creates an anonymous [ZkPage] and calls [ZkElement.launchBuild] on it with
         * the [builder] function.
         *
         * Use this function when you need to fetch data asynchronously during building
         * the element.
         */
        fun launchBuildNewPage(module: String, viewPrefix: String, builder: suspend ZkBuilder.() -> Unit) =
            // FIXME run build only when needed
            ZkPage(module, viewPrefix).launchBuild(builder) as ZkPage

        /**
         * Creates an anonymous [ZkPage] and calls [ZkElement.build] on it with
         * the [builder] function.
         *
         * Use this function when there is no need to asynchronous data fetch.
         */
        fun buildNewPage(module: String, viewPrefix: String, builder: ZkBuilder.() -> Unit) =
            ZkPage(module, viewPrefix).build(builder) as ZkPage
    }

    val path = "$module$viewPrefix"

    open fun open() = Application.changeNavState(path)

    override fun route(routing: AppRouting, state: NavState) = this

}