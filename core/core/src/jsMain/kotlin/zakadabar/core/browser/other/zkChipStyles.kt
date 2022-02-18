/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.other

import zakadabar.core.resource.css.*

var zkChipStyles by cssStyleSheet(ZkChipStyles())

open class ZkChipStyles : ZkCssStyleSheet() {

    open var iconSize by cssParameter { 24 }
    open var cancelIconSize by cssParameter { 18 }

    open val container by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        width = "fit-content"
        height = 32.px
        borderRadius = 16.px
    }

    open val icon by cssClass {
        + BoxSizing.borderBox

        marginLeft = 4.px
        marginRight = 8.px
        width = iconSize.px
        height = iconSize.px
    }

    open val cancelIcon by cssClass {
        + BoxSizing.borderBox
        + Cursor.pointer

        marginLeft = 8.px
        marginRight = 8.px
        width = cancelIconSize.px
        height = cancelIconSize.px
    }

    open val gap by cssClass {
        marginRight = 8.px
    }

    open val textLeftPaddingNoIcon by cssClass {
        paddingLeft = 12.px
    }

    open val textRightPaddingNoCancel by cssClass {
        paddingRight = 12.px
    }

    open val primaryFill by cssClass {
        backgroundColor = theme.primaryColor
        color = theme.primaryPair
        fill = theme.primaryPair
    }

    open val primaryNoFill by cssClass {
        color = theme.primaryColor
        fill = theme.primaryColor
    }

    open val primaryBorder by cssClass {
        border = "1px solid ${theme.primaryColor}"
    }

    open val primaryNoBorder by cssClass {
        // I used this way to ensure that the bordered and non-bordered
        // buttons have the same dimensions by default
        border = "1px solid transparent"
    }

    open val secondaryFill by cssClass {
        backgroundColor = theme.secondaryColor
        color = theme.secondaryPair
        fill = theme.secondaryPair
    }

    open val secondaryNoFill by cssClass {
        color = theme.secondaryColor
        fill = theme.secondaryColor
    }

    open val secondaryBorder by cssClass {
        border = "1px solid ${theme.secondaryColor}"
    }

    open val secondaryNoBorder by cssClass {
        // I used this way to ensure that the bordered and non-bordered
        // buttons have the same dimensions by default
        border = "1px solid transparent"
    }

    open val successFill by cssClass {
        backgroundColor = theme.successColor
        color = theme.successPair
        fill = theme.successPair

        hover {
            backgroundColor = theme.successColor + "80"
        }
    }

    open val successNoFill by cssClass {
        color = theme.successColor
        fill = theme.successColor
    }

    open val successBorder by cssClass {
        border = "1px solid ${theme.successColor}"
    }

    open val successNoBorder by cssClass {
        border = "1px solid transparent"
    }

    open val warningFill by cssClass {
        backgroundColor = theme.warningColor
        color = theme.warningPair
        fill = theme.warningPair
    }

    open val warningNoFill by cssClass {
        color = theme.warningColor
        fill = theme.warningColor
    }

    open val warningBorder by cssClass {
        border = "1px solid ${theme.warningColor}"
    }

    open val warningNoBorder by cssClass {
        border = "1px solid transparent"
    }

    open val dangerFill by cssClass {
        backgroundColor = theme.dangerColor
        color = theme.dangerPair
        fill = theme.dangerPair
    }

    open val dangerNoFill by cssClass {
        color = theme.dangerColor
        fill = theme.dangerColor
    }

    open val dangerBorder by cssClass {
        border = "1px solid ${theme.dangerColor}"
    }

    open val dangerNoBorder by cssClass {
        border = "1px solid transparent"
    }

    open val infoFill by cssClass {
        backgroundColor = theme.infoColor
        color = theme.infoPair
        fill = theme.infoPair
    }

    open val infoNoFill by cssClass {
        color = theme.infoColor
        fill = theme.infoColor
    }

    open val infoBorder by cssClass {
        border = "1px solid ${theme.infoColor}"
    }

    open val infoNoBorder by cssClass {
        border = "1px solid transparent"
    }

    open val disabledFill by cssClass {
        backgroundColor = theme.disabledColor
        color = theme.disabledPair
        fill = theme.disabledPair
    }

    open val disabledNoFill by cssClass {
        color = theme.disabledColor
        fill = theme.disabledColor
    }

    open val disabledBorder by cssClass {
        border = "1px solid ${theme.disabledColor}"
    }

    open val disabledNoBorder by cssClass {
        border = "1px solid transparent"
    }

}