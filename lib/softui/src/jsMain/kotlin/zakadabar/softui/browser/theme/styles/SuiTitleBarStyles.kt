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

     open val appHeader by cssClass {
        + Display.grid
        width = 100.percent
        height = 100.percent
        gridTemplateColumns = "max-content 1fr max-content"
        color = theme.infoPair
    }

    open val appHeaderText by cssClass {
        fontSize = 14.px
    }

    override val appTitleBar by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        + JustifyContent.spaceBetween
        + FlexWrap.wrap

        width = 100.percent
        height = 100.percent
        color = theme.textColor
        paddingTop = 6.px
        paddingBottom = 12.px

        paddingLeft = 20.px
        paddingRight = 20.px

        medium {
            paddingLeft = 10.px
            paddingRight = 10.px
        }
    }


    /**
     * Container for the title in the application title bar.
     */
    override val titleContainer by cssClass {
        + Display.flex
        fontSize = 24.px
        fontWeight = 400.weight
    }

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


    /**
     * When the side bar is closed a button is shown in the application title
     * bar that opens the sidebar again.
     */
    override val sidebarHandle by cssClass {
        + AlignSelf.center
        paddingLeft = 20.px
        paddingRight = 20.px
        fill = theme.infoPair
        + Cursor.pointer
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

    override val localTitleAndIcon by cssClass {
        marginTop = 4.px
        gridTemplateColumns = "24px 1fr"
        gap = 8.px
        gridAutoRows = "max-content"
    }
}