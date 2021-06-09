/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet
import zakadabar.stack.util.alpha

val zkFormStyles by cssStyleSheet(ZkFormStyles)

object ZkFormStyles : ZkCssStyleSheet() {

    var rowHeight = 38

    // -------------------------------------------------------------------------
    // Containers and layouts
    // -------------------------------------------------------------------------

    val outerContainer by cssClass {
        boxSizing = "border-box"
        display = "flex"
        flexDirection = "column"
        width = "100%"
    }

    val contentContainer by cssClass {
        flexGrow = "1"
    }

    val form by cssClass {

    }

    val onePanel by cssClass {
        display = "grid"
        gridTemplateColumns = "1fr"
        gap = theme.spacingStep
    }

    val twoPanels by cssClass {
        display = "grid"
        gridTemplateColumns = "1fr 1fr"
        gap = theme.spacingStep
    }

    val spanTwoPanels by cssClass {
        gridColumn = "1 / span 2"
    }

    // -------------------------------------------------------------------------
    // Buttons
    // -------------------------------------------------------------------------

    val buttons by cssClass {
        marginBlockStart = theme.spacingStep / 2
        marginBlockEnd = theme.spacingStep / 2
    }

    // -------------------------------------------------------------------------
    // Section
    // -------------------------------------------------------------------------

    val section by cssClass {
        display = "flex"
        flexDirection = "column"
        padding = 12
        paddingLeft = 20
        paddingBottom = 8
        boxShadow = theme.boxShadow
        borderRadius = theme.cornerRadius
        backgroundColor = theme.blockBackgroundColor
        border = theme.fixBorder
        marginBottom = theme.spacingStep / 2
    }

    val sectionTitle by cssClass {
        color = theme.textColor
        fontWeight = 500
        paddingBottom = 4
    }

    val sectionSummary by cssClass {
        color = theme.textColor
        paddingBottom = 16
    }

    // -------------------------------------------------------------------------
    // Field Base
    // -------------------------------------------------------------------------

    val fieldContainer by cssClass {
        boxSizing = "border-box"
        display = "contents"
    }

    val fieldLabel by cssClass {
        color = theme.textColor
        fontSize = "90%"
        fontWeight = "400"
        display = "flex"
        alignItems = "center"
        minHeight = rowHeight
        paddingRight = 8
        // this is problematic with ZkCheckboxList, paddingLeft = 8
        cursor = "default"
    }

    val mandatoryMark by cssClass {
        color = ZkColors.Red.c400
    }

    val fieldValue by cssClass {
        minHeight = rowHeight
        color = theme.inputTextColor
        marginBottom = 6
    }

    val fieldError by cssClass {
        color = ZkColors.Gray.c700
        fontSize = "90%"
        borderBottom = "1px solid ${ZkColors.Gray.c600}"
    }

    // -------------------------------------------------------------------------
    // Text
    // -------------------------------------------------------------------------

    private fun ZkCssStyleRule.fieldDefault() {
        fontFamily = theme.fontFamily
        fontSize = theme.fontSize
        fontWeight = 300
        display = "block"
        lineHeight = "1.3"
        padding = ".6em 1.4em .5em .4em"
        width = "100%"
        maxWidth = "100%"
        boxSizing = "border-box"
        margin = 0
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        border = "1px solid ${theme.borderColor}"
        borderRadius = theme.cornerRadius
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
            borderRadius = theme.cornerRadius
            outline = "none"
        }

        on(" option") {
            fontWeight = "normal"
        }

        on(":disabled") {
            color = theme.disabledInputTextColor
            backgroundColor = theme.disabledInputBackgroundColor

            // https://stackoverflow.com/questions/262158/disabled-input-text-color-on-ios
            styles["-webkit-text-fill-color"] = theme.disabledInputTextColor
            opacity = 1 /* required on iOS */
        }

    }

    val disabledString by cssClass {
        fieldDefault()

        // https://stackoverflow.com/questions/262158/disabled-input-text-color-on-ios
        styles["-webkit-text-fill-color"] = theme.disabledInputTextColor
        opacity = 1 /* required on iOS */

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
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        outline = "none"
    }

    val selectedOption by cssClass {
        display = "flex"

        fieldDefault()
        decorators()

        lineHeight = "1" // to prevent conversion to px
        height = rowHeight
        fill = theme.inputTextColor
        paddingRight = "0.3em"

        justifyContent = "space-between"
        alignItems = "center"

        on(" option") {
            fontWeight = "normal"
        }
    }

    val disabledSelect by cssClass {
        backgroundColor = theme.disabledInputBackgroundColor
        color = theme.disabledInputTextColor
    }

    val selectOptionList by cssClass {
        position = "absolute"
        zIndex = 100
        outline = "none"
        backgroundColor = theme.inputBackgroundColor
        boxShadow = "0 0 32px 12px rgba(0, 0, 0, 0.2) "
        borderRadius = theme.cornerRadius
        overflowY = "auto"
        overflowX = "hidden"
    }

    val selectEntry by cssClass {
        color = theme.inputTextColor
        width = 200
        minHeight = rowHeight
        borderBottom = "1px solid ${theme.borderColor}"
        display = "flex"
        alignItems = "center"
        paddingLeft = 8
        cursor = "pointer"

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
        display = "flex"
        paddingLeft = ".8em"
        height = rowHeight
        alignItems = "center"
        outline = "none"
    }

    // -------------------------------------------------------------------------
    // Iamge
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // Invalid field list
    // -------------------------------------------------------------------------

    val invalidFieldList by cssClass {
        padding = 12
        margin = 8
        boxShadow = theme.boxShadow
        borderRadius = theme.cornerRadius
        backgroundColor = ZkColors.white
    }

    val invalidFieldListInto by cssClass {
        fontSize = "80%"
        color = ZkColors.Gray.c600
        paddingBottom = 16
    }
}