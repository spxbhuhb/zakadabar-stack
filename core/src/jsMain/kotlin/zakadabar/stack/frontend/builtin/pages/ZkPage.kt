/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.application.ZkAppLayout
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkApplication.t
import zakadabar.stack.frontend.application.ZkNavState
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.util.plusAssign

/**
 * Provides common functions used in most page implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkPage(
    val layout: ZkAppLayout? = null,
    cssClass: String? = null
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

    var appTitle = true
    var titleText: String? = null
    var title: ZkAppTitle? = null

    override var viewName = "${this::class.simpleName}"

    open fun open() = ZkApplication.changeNavState("/$viewName")

    override fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement {
        if (layout != null) routing.nextLayout = layout
        return this
    }

    init {
        classList += cssClass ?: ZkPageStyles.scrollable
    }

    open fun onConfigure() {

    }

    override fun onCreate() {
        onConfigure()
        super.onCreate()
    }

    override fun onResume() {
        super.onResume()
        setAppTitle()
    }

    open fun setAppTitle() {
        if (! appTitle) return

        title?.let {
            ZkApplication.title = it
            return
        }

        val text = titleText ?: t(this::class.simpleName ?: "")
        ZkApplication.title = ZkAppTitle(text)
    }

}