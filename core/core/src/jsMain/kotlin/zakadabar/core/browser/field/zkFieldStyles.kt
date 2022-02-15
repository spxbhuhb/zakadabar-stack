/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

var zkFieldStyles by cssStyleSheet(ZkFieldStyles())

open class ZkFieldStyles : ZkCssStyleSheet() {

    open var fieldHeight by cssParameter { 38 }

    // -------------------------------------------------------------------------
    // Field Base
    // -------------------------------------------------------------------------

    open val fieldContainer by cssClass {
        + BoxSizing.borderBox
        + Display.contents
    }

    open val fieldLabel by cssClass {
        + Display.flex
        + AlignItems.center

        + Cursor.default

        fontSize = 90.percent
        fontWeight = 400.weight

        color = theme.textColor
        height = fieldHeight.px
        paddingRight = 8.px
    }

    open val mandatoryMark by cssClass {
        color = ZkColors.Red.c400
    }

    open val fieldValue by cssClass {
        minHeight = fieldHeight.px
        color = theme.inputTextColor
        width = 100.percent
    }

    open val fieldError by cssClass {
        color = ZkColors.Gray.c700
        fontSize = 90.percent
        borderBottom = "1px solid ${ZkColors.Gray.c600}"
    }

    // -------------------------------------------------------------------------
    // Text
    // -------------------------------------------------------------------------

    open fun ZkCssStyleRule.fieldDefault() {
        + Display.block
        + BoxSizing.borderBox

        fontFamily = theme.fontFamily
        fontSize = theme.fontSize
        fontWeight = 300.weight
        lineHeight = "1.3"
        padding = ".6em 1.4em .5em .4em"
        width = 100.percent
        maxWidth = 100.percent
        margin = 0.px
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        border = "1px solid ${theme.borderColor}"
        borderRadius = theme.cornerRadius.px
        color = theme.inputTextColor
        backgroundColor = theme.inputBackgroundColor
    }

    open fun ZkCssStyleRule.decorators() {
        on(".invalid") {
            backgroundColor = theme.dangerColor.alpha(0.1)
            border = "1px solid ${theme.dangerColor}"
        }

        on(".invalid:hover:not(:disabled):not(:focus)") {
            backgroundColor = theme.dangerColor.alpha(0.3)
        }

        on(".invalid:focus") {
            backgroundColor = theme.dangerColor.alpha(0.1)
            border = "1px solid ${theme.dangerColor}"
        }

        on(":hover:not(:disabled):not(:focus)") {
            color = theme.hoverTextColor
            backgroundColor = theme.infoColor.alpha(0.1)
        }

        on(":focus") {
            border = "1px solid ${theme.infoColor}"
            borderRadius = theme.cornerRadius.px
            outline = "none"
        }

        on(" option") {
            + FontWeight.normal
        }

        on(":disabled") {
            color = theme.disabledInputTextColor
            backgroundColor = theme.disabledInputBackgroundColor

            // https://stackoverflow.com/questions/262158/disabled-input-text-color-on-ios
            styles["-webkit-text-fill-color"] = theme.disabledInputTextColor
            opacity = 1.opacity /* required on iOS */
        }

    }

    open val disabledString by cssClass {
        fieldDefault()

        // https://stackoverflow.com/questions/262158/disabled-input-text-color-on-ios
        styles["-webkit-text-fill-color"] = theme.disabledInputTextColor
        opacity = 1.opacity /* required on iOS */

        color = theme.disabledInputTextColor
        backgroundColor = theme.disabledInputBackgroundColor

        outline = "none"
    }

    open val text by cssClass {
        fieldDefault()
        decorators()
    }

    open val textarea by cssClass {
        fieldDefault()
        decorators()
    }

    // -------------------------------------------------------------------------
    // Selects
    // -------------------------------------------------------------------------

    open val selectContainer by cssClass {
        + Position.relative
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        outline = "none"

        border = "1px solid ${theme.borderColor}"
        borderRadius = theme.cornerRadius.px
        color = theme.inputTextColor
        backgroundColor = theme.inputBackgroundColor

        decorators()
    }

    open val selectedOption by cssClass {
        fieldDefault()

        // these have to be after fieldDefault
        + Display.flex
        + JustifyContent.spaceBetween
        + AlignItems.center

        // these are set but the container
        border = "none"
        color = "inherit"
        backgroundColor = "inherit"

        lineHeight = "1" // to prevent conversion to px
        fill = theme.inputTextColor

        paddingTop = 0.px
        paddingBottom = 0.px
        paddingRight = 0.3.em

        on(" option") {
            + FontWeight.normal
        }
    }

    open val disabledSelect by cssClass {
        backgroundColor = theme.disabledInputBackgroundColor
        color = theme.disabledInputTextColor
    }

    open val selectOptionPopup by cssClass {
        + Position.fixed

        + Display.flex
        + FlexDirection.column

        + OverflowY.hidden
        + OverflowX.hidden

        zIndex = 100.zIndex

        outline = "none"
        boxShadow = "0 0 32px 12px rgba(0, 0, 0, 0.4) "
    }

    open val selectOptionFilter by cssClass {
        marginBottom = 10.px
    }

    open val selectOptionList by cssClass {

        + OverflowY.auto
        + OverflowX.hidden

        flexGrow = 1.0

        border = "1px solid ${theme.infoColor}"
        borderRadius = theme.cornerRadius.px

        backgroundColor = theme.inputBackgroundColor
    }

    open val selectEntry by cssClass {
        + Display.flex
        + AlignItems.center
        + Cursor.pointer

        color = theme.inputTextColor
        minWidth = 200.px
        maxWidth = 100.percent
        minHeight = fieldHeight.px
        borderBottom = "1px solid ${theme.borderColor}"
        paddingLeft = 8.px

        on(":hover") {
            color = theme.hoverTextColor
            backgroundColor = theme.hoverBackgroundColor
        }
    }

    open val selected by cssClass {
        color = ZkColors.white
        backgroundColor = theme.infoColor
    }


    open val radioGroupContainer by cssClass {
        fieldDefault()
        decorators()

        + Position.relative
        + Display.flex
        + FlexDirection.column
    }

    open val radioGroupItem by cssClass {
        height = fieldHeight.px
    }

    // -------------------------------------------------------------------------
    // Boolean
    // -------------------------------------------------------------------------

    open val booleanField by cssClass {
        + Display.flex
        + AlignItems.center

        height = fieldHeight.px
        outline = "none"
    }

}