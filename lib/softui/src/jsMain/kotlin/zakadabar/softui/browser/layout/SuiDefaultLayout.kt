/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.layout

import kotlinx.browser.window
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementState
import zakadabar.core.browser.application.ZkAppLayout
import zakadabar.core.browser.application.application
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.util.PublicApi
import zakadabar.softui.browser.theme.styles.SuiLayoutStyles
import zakadabar.softui.browser.theme.styles.suiLayoutStyles
import zakadabar.softui.browser.titlebar.SuiAppHeader
import zakadabar.softui.browser.titlebar.SuiAppTitleBar

open class SuiDefaultLayout(
    open val styles: SuiLayoutStyles = suiLayoutStyles
) : ZkAppLayout("default") {

    open var header = SuiAppHeader()
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
    protected var separatorContainer = ZkElement()
    protected var pageTitleContainer = SuiAppTitleBar()
    protected var sideBarContainer = ZkElement()
    protected var popupSidebarContainer = ZkElement()

    override fun onCreate() {
        super.onCreate()

        headerContainer css styles.headerContainer build { + header }

        separatorContainer css styles.separator

        sideBarContainer css styles.sideBarContainer build {
            + sideBar
        }

        popupSidebarContainer css styles.popupSideBarContainer

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
        onTitleChange(null)
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
            this -= separatorContainer
            this -= sideBarContainer
            this -= pageTitleContainer
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

        + headerContainer gridRow 1
        + separatorContainer gridRow 2
        + pageTitleContainer gridRow 3
        + contentContainer gridRow 4

        + popupSidebarContainer build {
            + sideBarContainer
        }

        popupSidebarContainer.hide()
    }

    open fun resumeLarge() {
        classList += suiLayoutStyles.defaultLayoutLarge
        contentContainer.classList += suiLayoutStyles.contentContainerLarge

        + headerContainer gridRow 1 gridColumn "1 / span 4"
        + separatorContainer gridRow 2 gridColumn "1 / span 4"
        + sideBarContainer gridRow "3 / span 4" gridColumn 1
        + pageTitleContainer gridRow 3 gridColumn 3
        + contentContainer gridRow 4 gridColumn 3

        sideBarContainer.show()
    }

    protected fun onTitleChange(newTitle: ZkAppTitle?) {
        pageTitleContainer.title  = newTitle
    }

    @PublicApi
    open fun onToggleSideBar() {
        if (activeMediaSize == MediaSize.Large) {
            sideBarContainer.toggle()
        } else {
            popupSidebarContainer.toggle()
        }
    }
}