/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.input

import zakadabar.core.browser.field.zkFieldStyles
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

var zkInputStyles by cssStyleSheet(ZkInputStyles())

open class ZkInputStyles : ZkCssStyleSheet() {

    open val checkboxList by cssClass {
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

    open val checkBoxOuter by cssClass {
        width = "max-content"
        outline = "none"
        border = "1px solid transparent"

        on(":focus") {
            border = "1px solid ${theme.infoColor}"
            borderRadius = theme.cornerRadius.px
            outline = "none"
        }

    }

    open val checkBoxNative by cssClass {
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

    open val checkboxLabel by cssClass {
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
    open val checkBoxMark by cssRule(
        """
            [type="checkbox"]:not(:checked) + label > svg
        """.trimIndent()
    ) {
        opacity = 0.opacity
    }

    open val radioOuter by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        outline = "none"
    }

    open val radioControlContainer by cssClass {
        + BoxSizing.borderBox

        marginRight = (theme.spacingStep / 2).px

        width = 22.px
        height = 22.px
        outline = "none"
        border = "1px solid transparent"

        on(":focus") {
            border = "1px solid ${theme.infoColor}"
            borderRadius = 10.px
            outline = "none"
        }
    }

    open val radioNative by cssClass {
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

    // this is not the text of the radio button but the icon
    open val radioLabel by cssClass {
        + Display.flex
        + JustifyContent.center
        + AlignItems.center
        + BoxSizing.borderBox

        width = 20.px
        height = 20.px

        padding = 1.px

        border = "1px solid ${theme.borderColor}"
        borderRadius = 10.px
        backgroundColor = theme.inputBackgroundColor
        fill = theme.inputTextColor
    }


    @Suppress("unused") // CSS selector, used by the browser
    open val radioMark by cssRule(
        """
            [type="radio"]:not(:checked) + label > svg
        """.trimIndent()
    ) {
        opacity = 0.opacity
    }

    open val radioText by cssClass {
        marginRight = (theme.spacingStep / 2).px
    }
}