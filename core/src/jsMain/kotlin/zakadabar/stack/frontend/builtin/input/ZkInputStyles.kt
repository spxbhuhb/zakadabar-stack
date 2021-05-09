/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.zkFormStyles
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet
import zakadabar.stack.util.alpha

val zkInputStyles by cssStyleSheet(ZkInputStyles())

open class ZkInputStyles : ZkCssStyleSheet<ZkTheme>() {

    val checkboxList by ZkFormStyles.cssClass {
        display = "flex"
        paddingLeft = ".8em"
        height = zkFormStyles.rowHeight
        alignItems = "center"

        on(":hover") {
            backgroundColor = ZkColors.LightBlue.c50
        }

        on(":focus") {
            backgroundColor = ZkColors.LightBlue.c50
            outline = "none"
        }
    }

    open val standaloneInput by cssClass {

        display = "block"
        color = "#444"
        padding = ".3em 1.4em .3em .8em"
        boxSizing = "border-box"
        margin = 0
        border = "1px solid #aaa"
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        backgroundColor = "#fff"
        borderRadius = 2
        minHeight = 26

        on(":hover") {
            borderColor = "#888"
        }

        on(":focus") {
            outline = "none"
        }

        on(" option") {
            fontWeight = "normal"
        }

        on(":disabled") {
            color = "gray"
            backgroundColor = "#gray"
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

    val checkBox by cssClass {
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