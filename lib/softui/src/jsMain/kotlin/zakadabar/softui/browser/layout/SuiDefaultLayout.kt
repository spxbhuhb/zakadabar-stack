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
import zakadabar.core.resource.css.px
import zakadabar.core.util.PublicApi
import zakadabar.softui.browser.theme.styles.SuiLayoutStyles
import zakadabar.softui.browser.theme.styles.suiLayoutStyles
import zakadabar.softui.browser.titlebar.SuiAppHeader
import zakadabar.softui.browser.titlebar.SuiAppTitleBar

open class SuiDefaultLayout(
    open val styles: SuiLayoutStyles = suiLayoutStyles,
    open val resizeSidebar: Boolean = false
) : ZkAppLayout("default") {

    companion object {
        const val SUI_SIDEBAR_WIDTH = "sui-sidebar-width"
    }

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

    open var sidebarWidth = application.loadState(SUI_SIDEBAR_WIDTH)?.toDoubleOrNull() ?: Double.NaN
        set(value) {
            field = value
            application.saveState(SUI_SIDEBAR_WIDTH, value.toString())
            setGridColumns()
        }

    enum class MediaSize {
        Uninitialized,
        Small,
        Large
    }

    var activeMediaSize = MediaSize.Uninitialized

    protected var headerContainer = ZkElement()
    protected var separatorContainer = ZkElement()
    lateinit var slider: ZkElement
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

        slider = if (resizeSidebar) SuiLayoutSlider(this) else ZkElement()

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
            this -= slider
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

        setGridColumns()

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

        setGridColumns()

        + headerContainer gridRow 1 gridColumn "1 / span 5"
        + separatorContainer gridRow 2 gridColumn "1 / span 5"
        + sideBarContainer gridRow "2 / span 4" gridColumn 2
        + slider gridRow "2 / span 4" gridColumn 3
        + pageTitleContainer gridRow 3 gridColumn 4
        + contentContainer gridRow 4 gridColumn 4

        sideBarContainer.show()
    }

    protected fun onTitleChange(newTitle: ZkAppTitle?) {
        pageTitleContainer.onTitleChange(newTitle)
    }

    @PublicApi
    open fun onToggleSideBar() {
        if (activeMediaSize == MediaSize.Large) {
            sideBarContainer.toggle()
            slider.toggle()
            setGridColumns()
        } else {
            popupSidebarContainer.toggle()
        }
    }

    open fun setGridColumns() {
        when (activeMediaSize) {
            MediaSize.Large -> setGridColumnsLarge()
            MediaSize.Small -> setGridColumnsSmall()
            else -> return
        }
    }

    open fun setGridColumnsLarge() {
        val leftMargin = when {
            ! resizeSidebar -> styles.sideBarLeftMargin
            sideBarContainer.isShown() -> styles.sideBarLeftMargin
            else -> 0
        }

        val sidebar = when {
            ! resizeSidebar -> "max-content"
            sidebarWidth.isNaN() -> if (sideBarContainer.isShown()) 240.px else 0.px
            else -> if (sideBarContainer.isShown()) sidebarWidth.px else 0.px
        }

        val sliderOrSerparator = when {
            ! resizeSidebar -> styles.gridMiddleWidth
            sideBarContainer.isShown() -> styles.gridSliderWidth
            else -> 0
        }

        gridTemplateColumns = "${leftMargin}px $sidebar ${sliderOrSerparator}px 1fr ${styles.contentRightMargin.px}"
    }

    open fun setGridColumnsSmall() {
        gridTemplateColumns = "1fr"
    }

}