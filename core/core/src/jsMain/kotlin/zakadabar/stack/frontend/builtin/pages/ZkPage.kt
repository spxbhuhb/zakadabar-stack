/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.application.ZkAppLayout
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkNavState
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleProvider
import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.util.newInstance
import zakadabar.stack.frontend.util.plusAssign

/**
 * Provides common functions used in most page implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkPage() : ZkElement(), ZkAppRouting.ZkTarget, ZkAppTitleProvider {

    /**
     * When true, the route method creates a new page instance each time.
     * When false, the current instance is used.
     */
    open val factory = true

    /**
     * The layout this page uses, when null, the default layout is used.
     */
    open var layout: ZkAppLayout? = null

    /**
     * Css this page starts with, [zkPageStypes.scrollable] when not specified.
     * When the page is create this Css class is added to the page.
     */
    open var css: ZkCssStyleRule? = null

    override var viewName = "${this::class.simpleName}"

    override var setAppTitle = true

    override var titleText: String? = null

    override var titleElement: ZkAppTitle? = null

    constructor(
        layout: ZkAppLayout? = null,
        css: ZkCssStyleRule? = null
    ) : this() {
        init(layout, css)
    }

    fun init(
        layout: ZkAppLayout? = null,
        css: ZkCssStyleRule? = null
    ) {
        this.layout = layout
        this.css = css
        classList += css ?: zkPageStyles.scrollable
    }

    override fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement {
        layout?.let { routing.nextLayout = it }
        return if (factory) {
            this::class.newInstance().also { it.init(layout, css) }
        } else {
            this
        }
    }

    open fun onConfigure() {

    }

    override fun onCreate() {
        onConfigure()
        super.onCreate()
    }

    override fun onResume() {
        super.onResume()
        setAppTitleBar()
    }

    open fun open() = application.changeNavState(this)

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
}