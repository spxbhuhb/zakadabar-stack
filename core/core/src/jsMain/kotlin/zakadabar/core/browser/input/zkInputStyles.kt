/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.input

import zakadabar.core.browser.field.zkFieldStyles
import zakadabar.core.browser.form.ZkFormStyles
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

val zkInputStyles by cssStyleSheet(ZkInputStyles())

open class ZkInputStyles : ZkCssStyleSheet() {

    val checkboxList by ZkFormStyles.cssClass {
        + Display.flex
        + AlignItems.center

        paddingLeft = .8.em
        height = zkFieldStyles.fieldHeight.px
    }

    @Suppress("DuplicatedCode") // better to keep form an standalone styles separated
    open val textInput by cssClass {
        + Display.block
        + BoxSizing.borderBox

        minWidth = 2.em
        fontFamily = theme.fontFamily
        fontSize = 80.percent
        fontWeight = 300.weight
        color = theme.inputTextColor
        padding = ".3em 1.4em .3em .8em"
        margin = 0.px
        border = "1px solid ${theme.borderColor}"
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        backgroundColor = theme.inputBackgroundColor
        borderRadius = theme.cornerRadius.px
        minHeight = 26.px

        on(":hover:not(:disabled):not(:focus)") {
            color = theme.hoverTextColor
            backgroundColor = theme.infoColor.alpha(0.1)
        }

        on(":focus") {
            border = "1px solid ${theme.infoColor}"
            borderRadius = theme.cornerRadius.px
            outline = "none"
        }

    }

    val checkBoxOuter by cssClass {
        width = "max-content"
        outline = "none"
        border = "1px solid transparent"

        on(":focus") {
            border = "1px solid ${theme.infoColor}"
            borderRadius = theme.cornerRadius.px
            outline = "none"
        }

    }

    val checkBoxNative by cssClass {
        + Display.none

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
        + Display.flex
        + JustifyContent.center
        + AlignItems.center

        padding = 1.px

        border = "1px solid ${theme.borderColor}"
        borderRadius = theme.cornerRadius.px
        backgroundColor = theme.inputBackgroundColor
        fill = theme.textColor
    }


    @Suppress("unused") // CSS selector, used by the browser
    val checkBoxMark by cssRule(
        """
            [type="checkbox"]:not(:checked) + label > svg
        """.trimIndent()
    ) {
        opacity = 0.opacity
    }
}