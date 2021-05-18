/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssParameter
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkButtonStyles by cssStyleSheet(ZkButtonStyles())

open class ZkButtonStyles : ZkCssStyleSheet() {

    open val iconSize by cssParameter { 20 }
    open val buttonHeight by cssParameter { 30 }

    open val text by cssClass {
        boxSizing = "border-box"
        height = buttonHeight
        width = "max-content"
        cursor = "pointer"
        display = "flex"
        justifyContent = "center"
        alignItems = "center"
        borderRadius = theme.cornerRadius
        paddingLeft = 10
        paddingRight = 10
        whiteSpace = "nowrap"

        on(":focus") {
            outline = "1px solid ${theme.infoColor}"
            styles["outline-offset"] = "4px"
        }
    }

    open val combined by cssClass {
        boxSizing = "border-box"
        height = buttonHeight
        width = "max-content"
        cursor = "pointer"
        display = "flex"
        flexDirection = "row"
        justifyContent = "center"
        alignItems = "center"
        borderRadius = theme.cornerRadius
        paddingRight = 10
        whiteSpace = "nowrap"

        on(":focus") {
            outline = "1px solid ${theme.infoColor}"
            styles["outline-offset"] = "4px"
        }

    }

    open val icon by cssClass {
        boxSizing = "border-box"
        display = "flex"
        justifyContent = "center"
        alignItems = "center"
        cursor = "pointer"
        width = buttonHeight
        height = buttonHeight

        on(":focus") {
            outline = "1px solid ${theme.infoColor}"
            styles["outline-offset"] = "4px"
        }
    }

    open val square by cssClass {
        borderRadius = theme.cornerRadius
        paddingTop = 0.25
    }

    open val round by cssClass {
        borderRadius = buttonHeight / 2
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