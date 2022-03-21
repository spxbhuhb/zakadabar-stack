/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import zakadabar.core.resource.css.*

var zkSideBarStyles by cssStyleSheet(ZkSideBarStyles())

open class ZkSideBarStyles : ZkCssStyleSheet() {

    open var backgroundColor by cssParameter { theme.backgroundColor }
    open var textColor by cssParameter { theme.textColor }
    open var itemMinHeight by cssParameter { 28 }
    open var fontSize by cssParameter { "80%" }
    open var iconSize by cssParameter { 18 }
    open var hoverTextColor by cssParameter { theme.hoverTextColor }
    open var sectionBackgroundColor by cssParameter { theme.blockBackgroundColor }
    open var sectionTextColor by cssParameter { theme.textColor }
    open var sectionBorderColor by cssParameter { theme.borderColor }

    open val sidebar by cssClass {
        + BoxSizing.borderBox
        + OverflowY.auto

        minHeight = 100.percent
        padding = (theme.spacingStep / 2).px

        fontSize = this@ZkSideBarStyles.fontSize
        backgroundColor = this@ZkSideBarStyles.backgroundColor
        color = this@ZkSideBarStyles.textColor

        small {
            padding = 0.px
        }

        medium {
            padding = 0.px
        }
    }

    open val item by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        + Cursor.pointer

        minHeight = itemMinHeight.px
        paddingRight = 8.px
        paddingLeft = 20.px

        color = this@ZkSideBarStyles.textColor

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@ZkSideBarStyles.hoverTextColor
        }
    }

    open val itemText by cssClass {
        flexGrow = 1.0
        color = "inherit"
    }

    open val icon by cssClass {
        fill = this@ZkSideBarStyles.textColor
        width = iconSize.px
        height = iconSize.px
        marginRight = (theme.spacingStep / 2).px
    }

    open fun ZkCssStyleRule.title() {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + JustifyContent.flexStart
        + AlignItems.center

        + Cursor.pointer

        minHeight = itemMinHeight.px

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@ZkSideBarStyles.hoverTextColor
        }

        on(" a") {
            flexGrow = 1.0
            color = "inherit"
        }
    }

    open val groupTitle by cssClass {
        title()

        paddingRight = 8.px
        //paddingLeft = 14.px

        fill = this@ZkSideBarStyles.textColor
        color = this@ZkSideBarStyles.textColor

    }

    open val groupArrow by cssClass {
        width = 20.px
    }

    open val groupContent by cssClass {
        paddingLeft = 20.px
    }

    open val sectionTitle by cssClass {
        title()

        marginTop = theme.spacingStep.px

        paddingRight = 8.px
        paddingLeft = 20.px

        backgroundColor = sectionBackgroundColor
        color = sectionTextColor

        //fill = this@ZkSideBarStyles.textColor.alpha(0.2)

        fill = sectionBorderColor
        borderBottom = "1px solid $sectionBorderColor"

        on(":first-child") {
            marginTop = 4.px
        }
    }

    open val sectionCloseIcon by cssClass {
        opacity = 0.opacity

        hover {
            opacity = 1.opacity
        }
    }

    open val sectionContent by cssClass {
        paddingLeft = 0.px
    }

    open val minimizedSectionContainer by cssClass {
        + Display.flex
        + FlexDirection.row
    }

    open val minimizedSection by cssClass {
        + Display.flex
        + JustifyContent.center
        + AlignItems.center

        + Cursor.pointer

        width = 28.px
        height = 28.px
        fontWeight = 500.weight
        fontSize = 125.percent
        backgroundColor = theme.blockBackgroundColor
        borderBottom = theme.fixBorder
        marginBottom = 6.px
    }
}