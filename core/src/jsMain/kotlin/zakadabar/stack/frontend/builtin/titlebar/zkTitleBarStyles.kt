/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.resources.css.*
import zakadabar.stack.util.alpha

var zkTitleBarStyles by cssStyleSheet(ZkTitleBarStyles())

open class ZkTitleBarStyles : ZkCssStyleSheet() {

    open var appTitleBarHeight by cssParameter { 44 }
    open var appHandleBackground by cssParameter { theme.backgroundColor }
    open var appHandleText by cssParameter { theme.textColor }
    open var appHandleBorder by cssParameter { theme.border }

    open var appTitleBarBackground by cssParameter { theme.backgroundColor }
    open var appTitleBarText by cssParameter { theme.textColor }
    open var appTitleBarBorder by cssParameter { theme.border }

    open var localTitleBarBackground by cssParameter { theme.textColor.alpha(0.1) }
    open var localTitleBarHeight by cssParameter { 32 }
    open var localTitleBarBorder by cssParameter { theme.fixBorder }

    /**
     * Application handle, the button and application name at the top left.
     * [appHandleContainer] is the style for the whole container.
     */
    val appHandleContainer by cssClass {
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
    val appHandleButton by cssClass {
        background = "transparent !important"
        fill = "$appHandleText !important"
        marginLeft = (theme.spacingStep / 2).px
        marginRight = (theme.spacingStep / 2).px
    }

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    val appTitleBar by cssClass {
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
    }

    /**
     * When the side bar is closed a button is shown in the application title
     * bar that opens the sidebar again.
     */
    val sidebarHandle by cssClass {
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
    val titleContainer by cssClass {
        marginLeft = theme.spacingStep.px
        + AlignItems.center
    }

    /**
     * Container for the context elements in the application title bar.
     */
    val contextElementContainer by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
    }

    /**
     * Container for the global elements in the application title bar.
     */
    val globalElementContainer by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        paddingRight = 10.px
    }

    val iconButton by cssClass {
        fill = "${theme.textColor} !important"
        backgroundColor = "transparent !important"
    }

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    val localTitleBar by cssClass {
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
    }
}