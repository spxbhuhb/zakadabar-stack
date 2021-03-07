/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.table.ZkTableStyles
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkFormStyles : ZkCssStyleSheet<ZkFormStyles>(ZkApplication.theme) {

    val outerContainer by ZkTableStyles.cssClass {
        display = "flex"
        flexDirection = "column"
        width = "100%"
        height = "100%"
    }

    val contentContainer by ZkTableStyles.cssClass {
        flexGrow = "1"
        padding = 10
        overflow = "scroll"
        backgroundColor = "rgb(245,245,245)"
    }

    val form by cssClass {
        backgroundColor = "rgb(245,245,245)"
    }

    val buttons by cssClass {
        margin = 8
    }

    val section by cssClass {
        display = "flex"
        flexDirection = "column"
        padding = 12
        margin = 8
        boxShadow = "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
        borderRadius = 2
        backgroundColor = ZkColors.white
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
        fontSize = "90%"
        fontWeight = "400"
        display = "flex"
        alignItems = "center"
        minHeight = theme.form.rowHeight
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

        decorators()
    }

    private fun ZkCssStyleRule.decorators() {
        on(".invalid") {
            backgroundColor = theme.form.invalidColor
        }

        on(".invalid:hover") {
            backgroundColor = theme.form.invalidColor
        }

        on(".invalid:focus") {
            backgroundColor = theme.form.invalidColor
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
            backgroundColor = ZkColors.Gray.c100
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

    val recordId by cssClass {
        fieldDefault()
        color = "#333"
        backgroundColor = ZkColors.Gray.c100
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
            backgroundImage =
                "url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22graytext%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E')," +
                        "linear-gradient(to bottom, #ffffff 0%,#e5e5e5 100%)"
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

        backgroundColor = "#f5f5f5"
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