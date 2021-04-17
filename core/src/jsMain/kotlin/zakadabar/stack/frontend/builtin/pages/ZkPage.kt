/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.application.ZkAppLayout
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkNavState
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkPageTitle
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBar
import zakadabar.stack.frontend.util.plusAssign

/**
 * Provides common functions used in most page implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkPage(
    val layout: ZkAppLayout? = null,
    val title: String? = null,
    val cssClasses: Array<out String>? = null
) : ZkElement(), ZkAppRouting.ZkTarget {

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

    constructor(layout: ZkAppLayout? = null, title: String? = null, vararg cssClasses: String) : this(layout, title, cssClasses)

    open fun open() = ZkApplication.changeNavState("/$viewName")

    override fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement {
        if (layout != null) routing.nextLayout = layout
        return this
    }

    override fun onCreate() {
        if (cssClasses == null) {
            classList += ZkPageStyles.page
        } else {
            for (cssClass in cssClasses) {
                classList += cssClass
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (title != null) ZkApplication.title = ZkPageTitle(title)
    }

}