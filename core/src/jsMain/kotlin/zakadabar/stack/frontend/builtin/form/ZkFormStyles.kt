/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.table.ZkTableStyles
import zakadabar.stack.frontend.resources.MaterialColors
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkFormStyles : CssStyleSheet<ZkFormStyles>(Application.theme) {

    val rowHeight = 38
    val invalidColor = MaterialColors.Red.c100

    val form by cssClass {
        backgroundColor = "rgb(245,245,245)"
        flexGrow = "1"
    }

    val contentContainer by ZkTableStyles.cssClass {
        flexGrow = "1"
        padding = 10
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
        backgroundColor = MaterialColors.white
    }

    val sectionTitle by cssClass {
        color = MaterialColors.black
        fontWeight = 500
        paddingBottom = 4
    }

    val sectionSummary by cssClass {
        fontSize = "80%"
        color = MaterialColors.Gray.c600
        paddingBottom = 16
    }

    val fieldContainer by cssClass {
        boxSizing = "border-box"
        display = "contents"
    }

    val fieldLabel by cssClass {
        color = MaterialColors.Gray.c700
        fontSize = "90%"
        display = "flex"
        alignItems = "center"
        minHeight = rowHeight
    }

    val fieldValue by cssClass {
        minHeight = rowHeight
    }

    val fieldBottomBorder by cssClass {
        gridColumnStart = 1
        gridColumnEnd = 3
        height = 1
        boxSizing = "border-box"
        borderBottom = "1px solid ${MaterialColors.Gray.c300}"

        on(".invalid") {
            borderBottom = "1px solid ${MaterialColors.Red.c500}"
        }
    }

    val onFieldHover by cssClass {
        borderBottom = "1px solid ${MaterialColors.LightBlue.a700}"
    }

    val fieldHint by cssClass {
        color = MaterialColors.Gray.c700
        fontSize = "90%"
        borderBottom = "1px solid ${MaterialColors.Gray.c600}"
    }

    val fieldError by cssClass {
        color = MaterialColors.Gray.c700
        fontSize = "90%"
        borderBottom = "1px solid ${MaterialColors.Gray.c600}"
    }

    val entryList by cssClass {
        width = 200
        height = 200
        border = "1px solid lightgray"
    }

    val recordId by cssClass {
        display = "block"
        fontSize = theme.fontSize
        fontFamily = theme.fontFamily
        fontWeight = theme.fontWeight
        color = "#444"
        lineHeight = "1.3"
        padding = ".6em 1.4em .5em .8em"
        width = "100%"
        maxWidth = "100%"
        boxSizing = "border-box"
        margin = 0
        border = 0
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        color = "#333"
        backgroundColor = MaterialColors.Gray.c100
    }

    val text by cssClass {
        display = "block"
        fontSize = theme.fontSize
        fontFamily = theme.fontFamily
        fontWeight = theme.fontWeight
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
        backgroundColor = "#fff"

        on(".invalid") {
            backgroundColor = invalidColor
        }

        on(".invalid:hover") {
            backgroundColor = invalidColor
        }

        on(".invalid:focus") {
            backgroundColor = invalidColor
        }

        on(":hover") {
            backgroundColor = MaterialColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = MaterialColors.LightBlue.c50
            outline = "none"
        }

        on(" option") {
            fontWeight = "normal"
        }

        on(":disabled") {
            color = "#333"
            backgroundColor = MaterialColors.Gray.c100
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

    val textarea by cssClass {
        flexGrow = "1"
        resize = "none"
        fontSize = theme.fontSize
        fontFamily = theme.fontFamily
        fontWeight = theme.fontWeight
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
        backgroundColor = "#fff"

        on(".invalid") {
            backgroundColor = invalidColor
        }

        on(".invalid:hover") {
            backgroundColor = invalidColor
        }

        on(".invalid:focus") {
            backgroundColor = invalidColor
        }

        on(":hover") {
            backgroundColor = MaterialColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = MaterialColors.LightBlue.c50
            outline = "none"
        }

        on(" option") {
            fontWeight = "normal"
        }

        on(":disabled") {
            color = "#333"
            backgroundColor = MaterialColors.Gray.c100
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

    val selectContainer by cssClass {
        display = "relative"
        outline = "none"

        on(".invalid") {
            backgroundColor = invalidColor
        }

        on(".invalid:hover") {
            backgroundColor = invalidColor
        }

        on(".invalid:focus") {
            backgroundColor = invalidColor
        }

        on(":hover") {
            backgroundColor = MaterialColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = MaterialColors.LightBlue.c50
            color = "#222"
            outline = "none"
        }

    }

    val selectedOption by cssClass {
        display = "block"
        fontSize = theme.fontSize
        fontFamily = theme.fontFamily
        fontWeight = theme.fontWeight
        color = "#444"
        padding = ".6em 1.4em .5em .8em"
        width = "100%"
        maxWidth = "100%"
        boxSizing = "border-box"
        margin = 0
        border = 0
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"


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
        backgroundColor = MaterialColors.white
        boxShadow = "0 0 32px 12px rgba(0, 0, 0, 0.2) "
        borderRadius = 2
        overflowY = "auto"
        overflowX = "hidden"
    }

    val selectEntry by cssClass {
        fontSize = theme.fontSize
        fontFamily = theme.fontFamily
        fontWeight = theme.fontWeight
        color = "#444"
        width = 200
        minHeight = rowHeight
        borderBottom = "1px solid ${MaterialColors.Gray.c300}"
        display = "flex"
        alignItems = "center"
        paddingLeft = 8
        cursor = "pointer"

        on(":hover") {
            color = "#444" // FIXME theme color
            backgroundColor = MaterialColors.LightBlue.c50
        }
    }

    val selected by cssClass {
        color = MaterialColors.white
        backgroundColor = MaterialColors.LightBlue.a700
    }

    val checkbox by cssClass {
        display = "flex"
        paddingLeft = ".8em"
        height = rowHeight
        alignItems = "center"

        on(":hover") {
            backgroundColor = MaterialColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = MaterialColors.LightBlue.c50
            outline = "none"
        }
    }

    init {
        attach()
    }
}