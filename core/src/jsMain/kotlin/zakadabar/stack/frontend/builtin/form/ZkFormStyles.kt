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

    val activeBlue = "#2746ab"

    val form by cssClass {
        backgroundColor = "rgb(245,245,245)"
        flexGrow = "1"
    }

    val contentContainer by ZkTableStyles.cssClass {
        flexGrow = "1"
        padding = 10
    }

    val headerTitle by cssClass {
        color = activeBlue
        fontSize = "120%"
        fontWeight = 500
        marginBottom = 8
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

    val invalid by cssClass {
        borderBottom = "1px solid red"
    }

    val selectedEntry by cssClass {
        width = 200
        height = 32
        border = "1px solid lightgray"
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

    // for select the styling idea comes from: https://www.filamentgroup.com/lab/select-css.html

    val select by cssClass {
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
        backgroundColor = "#fff"

        // Background image below uses 2 urls. The first is an svg data uri for the arrow icon, and the second is the gradient.
        // for the icon, if you want to change the color, be sure to use `%23` instead of `#`, since it's a url.
        // You can also swap in a different svg icon or an external image reference.

        backgroundImage =
            "url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22%23007CB2%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E')," +
                    "linear-gradient(to bottom, #ffffff 0%,#ffffff 100%)"
        backgroundRepeat = "no-repeat, repeat"
        backgroundPosition = "right .7em top 50%, 0 0"
        backgroundSize = ".65em auto, 100%"

        on("::-ms-expand") {
            display = "none"
        }

        on(":hover") {
            backgroundColor = MaterialColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = MaterialColors.LightBlue.c50
            //boxShadow = "0 0 1px 3px rgba(59, 153, 252, .7)"
            // box-shadow: 0 0 0 3px -moz-mac-focusring;
            color = "#222"
            outline = "none"
        }

        on(" option") {
            fontWeight = "normal"
        }

        ///* TODO Support for rtl text, explicit support for Arabic and Hebrew */
        //*[dir="rtl"] .select-css, :root:lang(ar) .select-css, :root:lang(iw) .select-css {
        //	background-position: left .7em top 50%, 0 0;
        //	padding: .6em .8em .5em 1.4em;
        //}

        on(":disabled") {
            color = "gray"
            backgroundImage =
                "url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22graytext%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E')," +
                        "linear-gradient(to bottom, #ffffff 0%,#e5e5e5 100%)"
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