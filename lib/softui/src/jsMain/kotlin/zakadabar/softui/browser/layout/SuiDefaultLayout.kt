/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.layout

import kotlinx.browser.window
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementState
import zakadabar.core.browser.application.ZkAppLayout
import zakadabar.core.browser.application.application
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.softui.browser.theme.styles.SuiLayoutStyles
import zakadabar.softui.browser.theme.styles.suiLayoutStyles

open class SuiDefaultLayout(
    open val styles : SuiLayoutStyles = suiLayoutStyles
) : ZkAppLayout("default") {

    open var header = ZkElement()
        set(value) {
            headerContainer -= field
            field = value
            headerContainer += field
        }

    open var sideBar = ZkElement()
        set(value) {
            sideBarContainer -= field
            field = value
            sideBarContainer += field
        }


    enum class MediaSize {
        Uninitialized,
        Small,
        Large
    }

    var activeMediaSize = MediaSize.Uninitialized

    protected var headerContainer = ZkElement()
    protected var sideBarContainer = ZkElement()
    protected var popupSidebarContainer = ZkElement()

    override fun onCreate() {
        super.onCreate()

        headerContainer css suiLayoutStyles.headerContainer build { + header }

        sideBarContainer css suiLayoutStyles.sideBarContainer build {
            + sideBar
        }

        popupSidebarContainer css suiLayoutStyles.popupSideBarContainer

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
        //header.title = application.title
        popupSidebarContainer.hide()

        val mediaSize = if (window.innerWidth < 800) MediaSize.Small else MediaSize.Large

        if (mediaSize == activeMediaSize) {
            super.onResume()
            return
        }

        if (activeMediaSize != MediaSize.Uninitialized) {
            classList -= suiLayoutStyles.defaultLayoutLarge
            classList -= suiLayoutStyles.defaultLayoutSmall

            contentContainer.classList -= suiLayoutStyles.contentContainerSmall
            contentContainer.classList -= suiLayoutStyles.contentContainerLarge

            this -= headerContainer
            this -= sideBarContainer
            this -= contentContainer
            this -= popupSidebarContainer
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
        classList += suiLayoutStyles.defaultLayoutSmall
        contentContainer.classList += suiLayoutStyles.contentContainerSmall

        + headerContainer
        + contentContainer

        + popupSidebarContainer build {
            + sideBarContainer
        }

        popupSidebarContainer.hide()
        //header.handleContainer.show()
    }

    open fun resumeLarge() {
        classList += suiLayoutStyles.defaultLayoutLarge
        contentContainer.classList += suiLayoutStyles.contentContainerLarge

        + headerContainer css zkLayoutStyles.grow gridColumn "1 / span 3"
        + sideBarContainer gridRow 2 gridColumn 1
        + contentContainer gridRow 2 gridColumn 3

        sideBarContainer.show()
        //header.handleContainer.hide()
    }

    override fun onPause() {
        //header.title = null
        super.onPause()
    }

    protected fun onTitleChange(newTitle: ZkAppTitle) {
        //this.header.title = newTitle
    }

    open fun onToggleSideBar() {
        if (activeMediaSize == MediaSize.Large) {
            sideBarContainer.toggle()
            //header.handleContainer.toggle()
        } else {
            popupSidebarContainer.toggle()
        }
    }
}