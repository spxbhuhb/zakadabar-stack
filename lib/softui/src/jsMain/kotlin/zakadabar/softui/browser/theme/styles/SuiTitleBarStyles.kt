/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.titlebar.TitleBarStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders

open class SuiTitleBarStyles : ZkCssStyleSheet(), TitleBarStyleSpec {

    override var appTitleBarHeight by cssParameter { 44 }
    override var appHandleBackground by cssParameter { theme.backgroundColor }
    override var appHandleText by cssParameter { theme.textColor }
    override var appHandleBorder by cssParameter { theme.border }

    override var appTitleBarBackground by cssParameter { theme.backgroundColor }
    override var appTitleBarText by cssParameter { theme.textColor }
    override var appTitleBarBorder by cssParameter { theme.border }

    override var localTitleBarBackground by cssParameter { theme.blockBackgroundColor }
    override var localTitleBarHeight by cssParameter { 42 }
    override var localTitleBarBorder by cssParameter { theme.fixBorder }
    override var localTitleBarText by cssParameter { theme.textColor }

    override val appHandleContainer by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        + WhiteSpace.nowrap
        + Cursor.pointer

        height = 100.percent

        paddingRight = 16.px

        large {
            borderRadius = Borders.borderRadius.md
        }

        fontSize = 14.px
        color = appHandleText
    }


    override val appHandleButton by cssClass {
        background = "transparent !important"
        fill = "$appHandleText !important"
        marginLeft = (theme.spacingStep / 2).px
        marginRight = (theme.spacingStep / 2).px
    }

    override val appTitleBar by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        width = "calc(100$ - ${theme.spacingStep / 2}px)"
        minHeight = appTitleBarHeight.px
        maxHeight = appTitleBarHeight.px
        borderBottom = appTitleBarBorder
        fontSize = 14.px

        large {
            borderRadius = Borders.borderRadius.md
        }

        color = appHandleText
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
        boxShadow = "none !important"
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
        width = 100.percent

        paddingLeft = 20.px

        borderBottom = localTitleBarBorder

        fontWeight = 400.weight
        fontSize = 16.px

        background = localTitleBarBackground
        color = localTitleBarText
        borderTopLeftRadius = Borders.borderRadius.md
        borderTopRightRadius = Borders.borderRadius.md
    }
}