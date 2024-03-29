/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.layout

import kotlinx.browser.window
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementState
import zakadabar.core.browser.application.ZkAppLayout
import zakadabar.core.browser.application.application
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.titlebar.ZkAppTitleBar
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign

/**
 * Default layout with app handle, title bar, sidebar and content.
 *
 * @property  spanHeader  When true, the app handle and the title bar is in a div and
 *                        that div spans the two columns of the first row. This has
 *                        effect only on large screens.
 */
open class ZkDefaultLayout(
    open val spanHeader: Boolean = false
) : ZkAppLayout("default") {

    open var appHandle = ZkElement()
        set(value) {
            appHandleContainer -= field
            field = value
            appHandleContainer += field
        }

    open var sideBar = ZkElement()
        set(value) {
            sideBarContainer -= field
            field = value
            sideBarContainer += field
        }

    open var titleBar = ZkAppTitleBar()
        set(value) {
            titleBarContainer -= field
            field = value
            titleBarContainer += field
        }

    enum class MediaSize {
        Uninitialized,
        Small,
        Large
    }

    var activeMediaSize = MediaSize.Uninitialized

    protected var appHandleContainer = ZkElement()
    protected var sideBarContainer = ZkElement()
    protected var titleBarContainer = ZkElement()

    protected var spanHeaderContainer = ZkElement()
    protected var popupSidebarContainer = ZkElement()

    override fun onCreate() {
        super.onCreate()

        appHandleContainer build { + appHandle }

        sideBarContainer css zkDefaultLayoutStyles.sideBarContainer build {
            + sideBar
        }

        titleBarContainer build { + titleBar }

        popupSidebarContainer css zkDefaultLayoutStyles.popupSideBarContainer

        on(window, "resize") {
            if (lifeCycleState != ZkElementState.Resumed) return@on
            val mediaSize = if (window.innerWidth < 800) MediaSize.Small else MediaSize.Large
            if (mediaSize != activeMediaSize) {
                onResume()
            }
        }

    }

    override fun onResume() {
        application.onTitleChange = ::onTitleChange
        titleBar.title = application.title
        popupSidebarContainer.hide()

        val mediaSize = if (window.innerWidth < 800) MediaSize.Small else MediaSize.Large

        if (mediaSize == activeMediaSize) {
            super.onResume()
            return
        }

        if (activeMediaSize != MediaSize.Uninitialized) {
            classList -= zkDefaultLayoutStyles.defaultLayoutLarge
            classList -= zkDefaultLayoutStyles.defaultLayoutSmall

            contentContainer.classList -= zkDefaultLayoutStyles.contentContainerSmall
            contentContainer.classList -= zkDefaultLayoutStyles.contentContainerLarge

            this -= appHandleContainer
            this -= sideBarContainer
            this -= titleBarContainer
            this -= contentContainer
            this -= popupSidebarContainer
            popupSidebarContainer -= appHandleContainer
            popupSidebarContainer -= sideBarContainer

            if (spanHeader) {
                this -= spanHeaderContainer
                spanHeaderContainer -= appHandleContainer
                spanHeaderContainer -= titleBarContainer
            }
        }

        activeMediaSize = mediaSize

        when (mediaSize) {
            MediaSize.Small -> resumeSmall()
            MediaSize.Large -> resumeLarge()
            MediaSize.Uninitialized -> throw IllegalStateException()
        }

        super.onResume()
    }

    open fun resumeSmall() {
        classList += zkDefaultLayoutStyles.defaultLayoutSmall
        contentContainer.classList += zkDefaultLayoutStyles.contentContainerSmall

        + titleBarContainer
        + contentContainer

        + popupSidebarContainer build {
            + appHandleContainer
            + sideBarContainer
        }

        popupSidebarContainer.hide()
        titleBar.handleContainer.show()
    }

    open fun resumeLarge() {
        classList += zkDefaultLayoutStyles.defaultLayoutLarge
        contentContainer.classList += zkDefaultLayoutStyles.contentContainerLarge

        if (spanHeader) {
            + spanHeaderContainer css zkLayoutStyles.row build {
                + appHandleContainer
                + titleBarContainer css zkLayoutStyles.grow
            } gridColumn "1 / span 2"
        } else {
            + appHandleContainer gridRow 1 gridColumn 1
            + titleBarContainer gridRow 1 gridColumn 2
        }
        + sideBarContainer gridRow 2 gridColumn 1
        + contentContainer gridRow 2 gridColumn 2

        appHandleContainer.show()
        sideBarContainer.show()
        titleBar.handleContainer.hide()
    }

    override fun onPause() {
        titleBar.title = null
        super.onPause()
    }

    protected fun onTitleChange(newTitle: ZkAppTitle) {
        this.titleBar.title = newTitle
    }

    open fun onToggleSideBar() {
        if (activeMediaSize == MediaSize.Large) {
            appHandleContainer.toggle()
            sideBarContainer.toggle()
            titleBar.handleContainer.toggle()
        } else {
            popupSidebarContainer.toggle()
        }
    }

}