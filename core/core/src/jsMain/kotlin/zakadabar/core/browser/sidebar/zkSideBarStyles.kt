/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.*

var zkSideBarStyles : SideBarStyleSpec by cssStyleSheet(ZkSideBarStyles())

open class ZkSideBarStyles : SideBarStyleSpec, ZkCssStyleSheet() {

    override var groupOpenIcon by cssParameter { ZkIcons.arrowRight }
    override var groupCloseIcon by cssParameter { ZkIcons.arrowDropDown }
    override var afterGroupOpenIcon by cssParameter { ZkIcons.arrowDropDown }
    override var afterGroupCloseIcon by cssParameter { ZkIcons.arrowDropUp }

    override var backgroundColor by cssParameter { theme.backgroundColor }
    override var textColor by cssParameter { theme.textColor }
    override var itemMinHeight by cssParameter { 28 }
    override var fontSize by cssParameter { "80%" }
    override var iconSize by cssParameter { 18 }
    override var hoverTextColor by cssParameter { theme.hoverTextColor }
    override var sectionBackgroundColor by cssParameter { theme.blockBackgroundColor }
    override var sectionTextColor by cssParameter { theme.textColor }
    override var sectionBorderColor by cssParameter { theme.borderColor }

    override val sidebar by cssClass {
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

    override val item by cssClass {
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

    override val itemText by cssClass {
        flexGrow = 1.0
        color = "inherit"
    }

    override val icon by cssClass {
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

    override val groupTitle by cssClass {
        title()

        paddingRight = 8.px
        //paddingLeft = 14.px

        fill = this@ZkSideBarStyles.textColor
        color = this@ZkSideBarStyles.textColor

    }

    override val groupArrow by cssClass {
        width = 20.px
    }

    override val groupContent by cssClass {
        paddingLeft = 20.px
    }

    override val sectionTitle by cssClass {
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

    override val sectionCloseIcon by cssClass {
        opacity = 0.opacity

        hover {
            opacity = 1.opacity
        }
    }

    override val sectionContent by cssClass {
        paddingLeft = 0.px
    }

}