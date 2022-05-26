/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.titlebar

import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

var zkTitleBarStyles : TitleBarStyleSpec by cssStyleSheet(ZkTitleBarStyles())

open class ZkTitleBarStyles : ZkCssStyleSheet(), TitleBarStyleSpec {

    override var appTitleBarHeight by cssParameter { 44 }
    override var appHandleBackground by cssParameter { theme.backgroundColor }
    override var appHandleText by cssParameter { theme.textColor }
    override var appHandleBorder by cssParameter { theme.border }

    override var appTitleBarBackground by cssParameter { theme.backgroundColor }
    override var appTitleBarText by cssParameter { theme.textColor }
    override var appTitleBarBorder by cssParameter { theme.border }

    override var localTitleBarBackground by cssParameter { theme.textColor.alpha(0.1) }
    override var localTitleBarHeight by cssParameter { 32 }
    override var localTitleBarBorder by cssParameter { theme.fixBorder }
    override var localTitleBarText by cssParameter { theme.textColor }

    /**
     * Application handle, the button and application name at the top left.
     * [appHandleContainer] is the style for the whole container.
     */
    override val appHandleContainer by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        + WhiteSpace.nowrap
        + Cursor.pointer

        minHeight = appTitleBarHeight.px
        maxHeight = appTitleBarHeight.px

        paddingRight = 16.px

        fontWeight = 500.weight
        fontSize = 120.percent

        borderBottom = appHandleBorder

        backgroundColor = appHandleBackground
        color = appHandleText
    }

    /**
     * Style for the button (a hamburger menu) in the application handle.
     */
    override val appHandleButton by cssClass {
        background = "transparent !important"
        fill = "$appHandleText !important"
        marginLeft = (theme.spacingStep / 2).px
        marginRight = (theme.spacingStep / 2).px
    }

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    override val appTitleBar by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        fontWeight = 400.px
        width = 100.percent
        minHeight = appTitleBarHeight.px
        maxHeight = appTitleBarHeight.px
        borderBottom = appTitleBarBorder
        fontSize = 16.px
        backgroundColor = appTitleBarBackground
        color = appTitleBarText
    }

    /**
     * When the side bar is closed a button is shown in the application title
     * bar that opens the sidebar again.
     */
    override val sidebarHandle by cssClass {
        + Display.flex
        + AlignItems.center
        + JustifyContent.center

        minHeight = appTitleBarHeight.px
        marginLeft = (theme.spacingStep / 2).px

        background = "transparent !important"
        fill = "$appTitleBarText !important"
    }

    /**
     * Container for the title in the application title bar.
     */
    override val titleContainer by cssClass {
        marginLeft = theme.spacingStep.px
        + AlignItems.center
    }

    /**
     * Container for the context elements in the application title bar.
     */
    override val contextElementContainer by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
    }

    /**
     * Container for the global elements in the application title bar.
     */
    override val globalElementContainer by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        paddingRight = 10.px
    }

    override val iconButton by cssClass {
        fill = "$appTitleBarText !important"
        backgroundColor = "transparent !important"
    }

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    override val localTitleBar by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        + JustifyContent.spaceBetween

        minHeight = localTitleBarHeight.px
        maxHeight = localTitleBarHeight.px
        width = 100.percent

        paddingLeft = 10.px

        borderBottom = localTitleBarBorder

        fontWeight = 400.weight
        fontSize = 16.px

        background = localTitleBarBackground
        color = localTitleBarText
    }

    override val localTitleAndIcon by cssClass {
        gridTemplateColumns = "24px 1fr"
        gap = 8.px
        gridAutoRows = "max-content"
    }
}