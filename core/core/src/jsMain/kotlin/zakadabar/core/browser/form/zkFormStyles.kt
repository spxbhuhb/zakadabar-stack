/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.form

import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

val zkFormStyles by cssStyleSheet(ZkFormStyles)

object ZkFormStyles : ZkCssStyleSheet() {

    var rowHeight = 38

    // -------------------------------------------------------------------------
    // Containers and layouts
    // -------------------------------------------------------------------------

    val outerContainer by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.column

        width = 100.percent
    }

    val contentContainer by cssClass {
        flexGrow = 1.0
    }

    val form by cssClass {

    }

    val onePanel by cssClass {
        + Display.grid

        gridTemplateColumns = 1.fr
        gap = theme.spacingStep.px
    }

    val twoPanels by cssClass {
        + Display.grid

        gridTemplateColumns = "1fr 1fr"
        gap = theme.spacingStep.px
    }

    val spanTwoPanels by cssClass {
        gridColumn = "1 / span 2"
    }

    // -------------------------------------------------------------------------
    // Buttons
    // -------------------------------------------------------------------------

    val buttons by cssClass {
        marginBlockStart = (theme.spacingStep / 2).px
        marginBlockEnd = (theme.spacingStep / 2).px
    }

    // -------------------------------------------------------------------------
    // Section
    // -------------------------------------------------------------------------

    val section by cssClass {
        + Display.flex
        + FlexDirection.column

        padding = 12.px
        paddingLeft = 20.px
        paddingBottom = 8.px
        marginBottom = (theme.spacingStep / 2).px

        boxShadow = theme.boxShadow
        borderRadius = theme.cornerRadius.px
        backgroundColor = theme.blockBackgroundColor
        border = theme.fixBorder
    }

    val sectionTitle by cssClass {
        color = theme.textColor
        fontWeight = 500.weight
        paddingBottom = 4.px
    }

    val sectionSummary by cssClass {
        color = theme.textColor
        paddingBottom = 16.px
    }

    // -------------------------------------------------------------------------
    // Field Base
    // -------------------------------------------------------------------------

    val fieldContainer by cssClass {
        + BoxSizing.borderBox
        + Display.contents
    }

    val fieldLabel by cssClass {
        + Display.flex
        + AlignItems.center

        + Cursor.default

        fontSize = 90.percent
        fontWeight = 400.weight

        color = theme.textColor
        minHeight = rowHeight.px
        paddingRight = 8.px
    }

    val mandatoryMark by cssClass {
        color = ZkColors.Red.c400
    }

    val fieldValue by cssClass {
        minHeight = rowHeight.px
        color = theme.inputTextColor
        marginBottom = 6.px
    }

    val fieldValueDense by cssClass {
        minHeight = rowHeight.px
        color = theme.inputTextColor
    }

    val fieldError by cssClass {
        color = ZkColors.Gray.c700
        fontSize = 90.percent
        borderBottom = "1px solid ${ZkColors.Gray.c600}"
    }

    // -------------------------------------------------------------------------
    // Text
    // -------------------------------------------------------------------------

    private fun ZkCssStyleRule.fieldDefault() {
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

    private fun ZkCssStyleRule.decorators() {
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

    val disabledString by cssClass {
        fieldDefault()

        // https://stackoverflow.com/questions/262158/disabled-input-text-color-on-ios
        styles["-webkit-text-fill-color"] = theme.disabledInputTextColor
        opacity = 1.opacity /* required on iOS */

        color = theme.disabledInputTextColor
        backgroundColor = theme.disabledInputBackgroundColor

        outline = "none"
    }

    val text by cssClass {
        fieldDefault()
        decorators()
    }

    val textarea by cssClass {
        fieldDefault()
        decorators()
    }

    // -------------------------------------------------------------------------
    // Selects
    // -------------------------------------------------------------------------

    val selectContainer by cssClass {
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

    val selectedOption by cssClass {
        + Display.flex

        fieldDefault()

        // these have to be after fieldDefault
        + JustifyContent.spaceBetween
        + AlignItems.center

        // these are set but the container
        border = "none"
        color = "inherit"
        backgroundColor = "inherit"

        lineHeight = "1" // to prevent conversion to px
        height = (rowHeight - 2).px// -2 to compensate border from container
        fill = theme.inputTextColor
        paddingRight = 0.3.em


        on(" option") {
            + FontWeight.normal
        }
    }

    val disabledSelect by cssClass {
        backgroundColor = theme.disabledInputBackgroundColor
        color = theme.disabledInputTextColor
    }

    val selectOptionList by cssClass {
        + Position.fixed

        + OverflowY.auto
        + OverflowX.hidden

        zIndex = 100.zIndex
        outline = "none"
        backgroundColor = theme.inputBackgroundColor
        boxShadow = "0 0 32px 12px rgba(0, 0, 0, 0.2) "
        borderRadius = theme.cornerRadius.px
    }

    val selectEntry by cssClass {
        + Display.flex
        + AlignItems.center
        + Cursor.pointer

        color = theme.inputTextColor
        width = 200.px
        minHeight = rowHeight.px
        borderBottom = "1px solid ${theme.borderColor}"
        paddingLeft = 8.px

        on(":hover") {
            color = theme.hoverTextColor
            backgroundColor = theme.hoverBackgroundColor
        }
    }

    val selected by cssClass {
        color = ZkColors.white
        backgroundColor = theme.infoColor
    }

    // -------------------------------------------------------------------------
    // Boolean
    // -------------------------------------------------------------------------

    val booleanField by cssClass {
        + Display.flex
        + AlignItems.center

        paddingLeft = .8.em
        height = rowHeight.px
        outline = "none"
    }

    // -------------------------------------------------------------------------
    // Iamge
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // Invalid field list
    // -------------------------------------------------------------------------

    val invalidFieldList by cssClass {
        padding = 12.px
        margin = 8.px
        boxShadow = theme.boxShadow
        borderRadius = theme.cornerRadius.px
        backgroundColor = ZkColors.white
    }

    val invalidFieldListInto by cssClass {
        fontSize = 80.percent
        color = ZkColors.Gray.c600
        paddingBottom = 16.px
    }
}