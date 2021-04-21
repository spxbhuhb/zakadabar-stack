/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.table.ZkTableStyles
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkFormStyles : ZkCssStyleSheet<ZkTheme>() {

    val outerContainer by ZkTableStyles.cssClass {
        display = "flex"
        flexDirection = "column"
        width = "100%"
    }

    val contentContainer by ZkTableStyles.cssClass {
        flexGrow = "1"
        overflow = "scroll"
    }

    val form by cssClass {

    }

    val onePanel by cssClass {
        display = "grid"
        gridTemplateColumns = "1fr"
        gap = theme.layout.spacingStep
    }

    val twoPanels by cssClass {
        display = "grid"
        gridTemplateColumns = "1fr 1fr"
        gap = theme.layout.spacingStep
    }

    val spanTwoPanels by cssClass {
        gridColumn = "1 / span 2"
    }

    val buttons by cssClass {

    }

    val section by cssClass {
        display = "flex"
        flexDirection = "column"
        padding = 12
        boxShadow = "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
        borderRadius = 2
        backgroundColor = ZkColors.white
        marginBottom = theme.layout.spacingStep / 2
    }

    val sectionTitle by cssClass {
        color = ZkColors.Blue.c700
        fontWeight = 500
        paddingBottom = 4
    }

    val sectionSummary by cssClass {
        fontSize = "80%"
        color = ZkColors.Gray.c600
        paddingBottom = 16
    }

    val fieldContainer by cssClass {
        boxSizing = "border-box"
        display = "contents"
    }

    val fieldLabel by cssClass {
        color = ZkColors.Gray.c700
        backgroundColor = theme.form.labelBackground
        fontSize = "90%"
        fontWeight = "400"
        display = "flex"
        alignItems = "center"
        minHeight = theme.form.rowHeight
        paddingRight = 8
        paddingLeft = 8
        borderRadius = 2
        cursor = "default"
    }

    val mandatoryMark by cssClass {
        color = ZkColors.Red.c400
    }

    val fieldValue by cssClass {
        minHeight = theme.form.rowHeight
    }

    val fieldBottomBorder by cssClass {
        gridColumnStart = 1
        gridColumnEnd = 3
        height = 1
        boxSizing = "border-box"
        borderBottom = "1px solid ${ZkColors.Gray.c300}"

        on(".invalid") {
            borderBottom = "1px solid ${ZkColors.Red.c500}"
        }
    }

    val onFieldHover by cssClass {
        borderBottom = "1px solid ${ZkColors.LightBlue.a700}"
    }

    val fieldError by cssClass {
        color = ZkColors.Gray.c700
        fontSize = "90%"
        borderBottom = "1px solid ${ZkColors.Gray.c600}"
    }

    private fun ZkCssStyleRule.fieldDefault() {
        fontFamily = theme.font.family
        fontSize = theme.font.size
        fontWeight = 300
        display = "block"
        color = "#444"
        lineHeight = "1.3"
        padding = ".6em 1.4em .5em .4em"
        width = "100%"
        maxWidth = "100%"
        boxSizing = "border-box"
        margin = 0
        border = 0
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        backgroundColor = theme.form.valueBackground

        decorators()
    }

    private fun ZkCssStyleRule.decorators() {
        on(".invalid") {
            backgroundColor = theme.form.invalidBackground
        }

        on(".invalid:hover") {
            backgroundColor = theme.form.invalidBackground
        }

        on(".invalid:focus") {
            backgroundColor = theme.form.invalidBackground
        }

        on(":hover") {
            backgroundColor = ZkColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = ZkColors.LightBlue.c50
            outline = "none"
        }

        on(" option") {
            fontWeight = "normal"
        }

        on(":disabled") {
            color = "#333"
            backgroundColor = theme.form.disabledBackground
            borderColor = "#aaa"
        }

        on(":disabled:hover") {
            borderColor = "#aaa"
        }

        on("[aria-disabled=true]") {
            color = "gray"
            borderColor = "#aaa"
        }
    }

    val disabledString by cssClass {
        fieldDefault()
        color = "#333"
        backgroundColor = theme.form.disabledBackground
        outline = "none"
    }

    val text by cssClass {
        fieldDefault()
    }

    val textarea by cssClass {
        fieldDefault()
        backgroundColor = ZkColors.white
    }

    val selectContainer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        outline = "none"

        decorators()
    }

    val selectedOption by cssClass {
        display = "flex"

        fieldDefault()

        height = theme.form.rowHeight
        alignItems = "center"

        on(" option") {
            fontWeight = "normal"
        }

        on(":disabled:hover") {
            borderColor = "#aaa"
        }

        on("[aria-disabled=true]") {
            color = "gray"
            backgroundColor = theme.form.disabledBackground
            borderColor = "#aaa"
        }
    }

    val selectOptionList by cssClass {
        position = "absolute"
        zIndex = 100
        outline = "none"
        backgroundColor = ZkColors.white
        boxShadow = "0 0 32px 12px rgba(0, 0, 0, 0.2) "
        borderRadius = 2
        overflowY = "auto"
        overflowX = "hidden"
    }

    val selectEntry by cssClass {
        color = "#444"
        width = 200
        minHeight = theme.form.rowHeight
        borderBottom = "1px solid ${ZkColors.Gray.c300}"
        display = "flex"
        alignItems = "center"
        paddingLeft = 8
        cursor = "pointer"

        on(":hover") {
            color = "#444" // FIXME theme color
            backgroundColor = ZkColors.LightBlue.c50
        }
    }

    val selected by cssClass {
        color = ZkColors.white
        backgroundColor = ZkColors.LightBlue.a700
    }

    val checkbox by cssClass {
        display = "flex"
        paddingLeft = ".8em"
        height = theme.form.rowHeight
        alignItems = "center"

        on(":hover") {
            backgroundColor = ZkColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = ZkColors.LightBlue.c50
            outline = "none"
        }
    }

    val invalidFieldList by cssClass {
        padding = 12
        margin = 8
        boxShadow = "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
        borderRadius = 2
        backgroundColor = ZkColors.white
    }

    val invalidFieldListInto by cssClass {
        fontSize = "80%"
        color = ZkColors.Gray.c600
        paddingBottom = 16
    }

    val imageDropArea by cssClass {
        boxSizing = "border-box"
        flexGrow = 1
        width = "100%"
        height = "100%"
        display = "flex"
        flexDirection = "row"
        justifyContent = "center"
        alignItems = "center"
        color = ZkColors.Gray.c800
        fill = ZkColors.Gray.c800

        padding = 20

        backgroundColor = theme.form.labelBackground
        borderRadius = 2
        border = "1px dotted lightgray"

        on(":hover") {
            backgroundColor = ZkColors.LightBlue.c50
        }
    }

    val imageDropAreaMessage by cssClass {
        fontSize = 14
        fontWeight = 400
        paddingLeft = 6
    }

    init {
        attach()
    }
}