/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkInputStyles by cssStyleSheet(ZkInputStyles())

open class ZkInputStyles : ZkCssStyleSheet<ZkTheme>() {

    val checkboxList by ZkFormStyles.cssClass {
        display = "flex"
        paddingLeft = ".8em"
        height = ZkFormStyles.theme.form.rowHeight
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
}