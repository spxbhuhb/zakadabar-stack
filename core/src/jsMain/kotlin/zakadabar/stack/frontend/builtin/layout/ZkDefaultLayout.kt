/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import kotlinx.browser.window
import zakadabar.stack.frontend.application.ZkAppLayout
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementState
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign

open class ZkDefaultLayout : ZkAppLayout("default") {

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

    private var appHandleContainer = ZkElement()
    private var sideBarContainer = ZkElement()
    private var titleBarContainer = ZkElement()

    private var popupSidebarContainer = ZkElement()

    override fun onCreate() {
        super.onCreate()

        appHandleContainer build { + appHandle }

        sideBarContainer css ZkLayoutStyles.sideBarContainer build {
            + sideBar
        }

        titleBarContainer build { + titleBar }

        contentContainer css ZkLayoutStyles.contentContainer

        popupSidebarContainer css ZkLayoutStyles.popupSideBarContainer

        on(window, "resize") {
            if (lifeCycleState != ZkElementState.Resumed) return@on
            val mediaSize = if (window.innerWidth < 800) MediaSize.Small else MediaSize.Large
            if (mediaSize != activeMediaSize) {
                onResume()
            }
        }

    }

    override fun onResume() {
        ZkApplication.onTitleChange = ::onTitleChange
        titleBar.title = ZkApplication.title
        popupSidebarContainer.hide()

        val mediaSize = if (window.innerWidth < 800) MediaSize.Small else MediaSize.Large

        if (mediaSize == activeMediaSize) {
            super.onResume()
            return
        }

        if (activeMediaSize != MediaSize.Uninitialized) {
            classList -= ZkLayoutStyles.defaultLayoutLarge
            classList -= ZkLayoutStyles.defaultLayoutSmall
            this -= appHandleContainer
            this -= sideBarContainer
            this -= titleBarContainer
            this -= contentContainer
            this -= popupSidebarContainer
            popupSidebarContainer -= appHandleContainer
            popupSidebarContainer -= sideBarContainer
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
        classList += ZkLayoutStyles.defaultLayoutSmall

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
        classList += ZkLayoutStyles.defaultLayoutLarge

        + appHandleContainer gridRow 1 gridColumn 1
        + sideBarContainer gridRow 2 gridColumn 1
        + titleBarContainer gridRow 1 gridColumn 2
        + contentContainer gridRow 2 gridColumn 2

        appHandleContainer.show()
        sideBarContainer.show()
        titleBar.handleContainer.hide()
    }

    override fun onPause() {
        titleBar.title = null
        super.onPause()
    }

    private fun onTitleChange(newTitle: ZkAppTitle) {
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