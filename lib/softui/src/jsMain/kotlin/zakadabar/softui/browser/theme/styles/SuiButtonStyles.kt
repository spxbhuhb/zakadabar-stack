/*
 * Copyright © 2020-2022, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.button.ButtonStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

open class SuiButtonStyles : ZkCssStyleSheet(), ButtonStyleSpec {

    override val iconSize by cssParameter { 20 }
    override val buttonHeight by cssParameter { 30 }

    override val text by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + JustifyContent.center
        + AlignItems.center

        + WhiteSpace.nowrap
        + Cursor.pointer

        + TextTransform.uppercase

        height = buttonHeight.px
        width = "max-content"
        minWidth = 100.px

        fontSize = 12.px
        fontWeight = 400.weight

        borderRadius = Borders.borderRadius.md
        boxShadow = BoxShadows.md
        paddingLeft = 10.px
        paddingRight = 10.px

        on(":focus") {
            outline = "1px solid ${theme.infoColor}"
            styles["outline-offset"] = "4px"
        }
    }

    override val combined by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + JustifyContent.center
        + AlignItems.center

        + Cursor.pointer
        + WhiteSpace.nowrap

        + TextTransform.uppercase

        height = buttonHeight.px
        width = "max-content"

        borderRadius = Borders.borderRadius.md
        boxShadow = BoxShadows.md

        paddingRight = 10.px

        fontSize = 12.px
        fontWeight = 400.weight

        on(":focus") {
            outline = "1px solid ${theme.infoColor}"
            styles["outline-offset"] = "4px"
        }

    }

    override val icon by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + JustifyContent.center
        + AlignItems.center

        + Cursor.pointer

        width = buttonHeight.px
        height = buttonHeight.px

        boxShadow = BoxShadows.md

        on(":focus") {
            outline = "1px solid ${theme.infoColor}"
            styles["outline-offset"] = "4px"
        }
    }

    override val noShadow by cssClass {
        boxShadow = "none"
    }

    override val square by cssClass {
        borderRadius = theme.cornerRadius.px
        paddingTop = 0.25.px
    }

    override val round by cssClass {
        borderRadius = (buttonHeight / 2).px
    }

    override val primaryFill by cssClass {
        backgroundColor = theme.primaryColor
        color = theme.primaryPair
        fill = theme.primaryPair
    }

    override val primaryNoFill by cssClass {
        color = theme.primaryColor
        fill = theme.primaryColor
    }

    override val primaryBorder by cssClass {
        border = "1px solid ${theme.primaryColor}"
    }

    override val primaryNoBorder by cssClass {
        // I used this way to ensure that the bordered and non-bordered
        // buttons have the same dimensions by default
        border = "1px solid transparent"
    }

    override val secondaryFill by cssClass {
        backgroundColor = theme.secondaryColor
        color = theme.secondaryPair
        fill = theme.secondaryPair
    }

    override val secondaryNoFill by cssClass {
        color = theme.secondaryColor
        fill = theme.secondaryColor
    }

    override val secondaryBorder by cssClass {
        border = "1px solid ${theme.secondaryColor}"
    }

    override val secondaryNoBorder by cssClass {
        // I used this way to ensure that the bordered and non-bordered
        // buttons have the same dimensions by default
        border = "1px solid transparent"
    }

    override val successFill by cssClass {
        backgroundColor = theme.successColor
        color = theme.successPair
        fill = theme.successPair

        hover {
            backgroundColor = theme.successColor + "80"
        }
    }

    override val successNoFill by cssClass {
        color = theme.successColor
        fill = theme.successColor
    }

    override val successBorder by cssClass {
        border = "1px solid ${theme.successColor}"
    }

    override val successNoBorder by cssClass {
        border = "1px solid transparent"
    }
    
    override val warningFill by cssClass {
        backgroundColor = theme.warningColor
        color = theme.warningPair
        fill = theme.warningPair
    }

    override val warningNoFill by cssClass {
        color = theme.warningColor
        fill = theme.warningColor
    }

    override val warningBorder by cssClass {
        border = "1px solid ${theme.warningColor}"
    }

    override val warningNoBorder by cssClass {
        border = "1px solid transparent"
    }

    override val dangerFill by cssClass {
        backgroundColor = theme.dangerColor
        color = theme.dangerPair
        fill = theme.dangerPair
    }

    override val dangerNoFill by cssClass {
        color = theme.dangerColor
        fill = theme.dangerColor
    }

    override val dangerBorder by cssClass {
        border = "1px solid ${theme.dangerColor}"
    }

    override val dangerNoBorder by cssClass {
        border = "1px solid transparent"
    }

    override val infoFill by cssClass {
        backgroundColor = theme.infoColor
        color = theme.infoPair
        fill = theme.infoPair
    }

    override val infoNoFill by cssClass {
        color = theme.infoColor
        fill = theme.infoColor
    }

    override val infoBorder by cssClass {
        border = "1px solid ${theme.infoColor}"
    }

    override val infoNoBorder by cssClass {
        border = "1px solid transparent"
    }

    override val disabledFill by cssClass {
        backgroundColor = theme.disabledColor
        color = theme.disabledPair
        fill = theme.disabledPair
    }

    override val disabledNoFill by cssClass {
        color = theme.disabledPair
        fill = theme.disabledPair
    }

    override val disabledBorder by cssClass {
        border = "1px solid ${theme.disabledColor}"
    }

    override val disabledNoBorder by cssClass {
        border = "1px solid transparent"
    }

}