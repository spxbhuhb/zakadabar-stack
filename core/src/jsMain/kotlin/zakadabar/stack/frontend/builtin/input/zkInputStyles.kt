/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.zkFormStyles
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet
import zakadabar.stack.util.alpha

val zkInputStyles by cssStyleSheet(ZkInputStyles())

open class ZkInputStyles : ZkCssStyleSheet() {

    val checkboxList by ZkFormStyles.cssClass {
        display = "flex"
        paddingLeft = ".8em"
        height = zkFormStyles.rowHeight
        alignItems = "center"
    }

    @Suppress("DuplicatedCode") // better to keep form an standalone styles separated
    open val textInput by cssClass {
        fontFamily = theme.fontFamily
        fontSize = "80%"
        fontWeight = 300
        display = "block"
        color = theme.inputTextColor
        padding = ".3em 1.4em .3em .8em"
        boxSizing = "border-box"
        margin = 0
        border = "1px solid ${theme.borderColor}"
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        backgroundColor = theme.inputBackgroundColor
        borderRadius = theme.cornerRadius
        minHeight = 26

        on(":hover:not(:disabled):not(:focus)") {
            color = theme.hoverTextColor
            backgroundColor = theme.infoColor.alpha(0.1)
        }

        on(":focus") {
            border = "1px solid ${theme.infoColor}"
            borderRadius = theme.cornerRadius
            outline = "none"
        }

    }

    val checkBoxOuter by cssClass {
        width = "max-content"
        outline = "none"

        on(":focus") {
            border = "1px solid ${theme.infoColor}"
            borderRadius = theme.cornerRadius
            outline = "none"
        }

    }

    val checkBoxNative by cssClass {
        display = "none"

        on(":hover:not(:disabled) + label") {
            color = theme.hoverTextColor
            backgroundColor = theme.infoColor.alpha(0.1)
        }

        on(":disabled:hover + label") {
            color = theme.disabledInputTextColor
            backgroundColor = theme.disabledInputBackgroundColor
        }

        on(":disabled + label") {
            color = theme.disabledInputTextColor
            backgroundColor = theme.disabledInputBackgroundColor
        }
    }

    val checkboxLabel by cssClass {
        border = "1px solid ${theme.borderColor}"
        borderRadius = theme.cornerRadius
        display = "flex"
        justifyContent = "center"
        alignItems = "center"
        backgroundColor = theme.inputBackgroundColor
        fill = theme.textColor
        padding = 1
    }


    @Suppress("unused") // CSS selector, used by the browser
    val checkBoxMark by cssRule(
        """
            [type="checkbox"]:not(:checked) + label > svg
        """.trimIndent()
    ) {
        opacity = 0
    }
}