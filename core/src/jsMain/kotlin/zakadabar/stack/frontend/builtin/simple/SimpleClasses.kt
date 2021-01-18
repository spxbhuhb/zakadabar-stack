/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.simple

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.CssStyleSheet

class SimpleClasses(theme: ZkTheme) : CssStyleSheet<SimpleClasses>(theme) {

    companion object {
        var simpleClasses = SimpleClasses(Application.theme).attach()
    }

    val button by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        color = theme.darkestGray
        textTransform = "uppercase"
        cursor = "pointer"
        backgroundColor = theme.lightestColor
        padding = 10
        borderWidth = 1
        borderStyle = "solid"
        borderColor = theme.darkestGray
        borderRadius = 4
        on(":hover") {
            backgroundColor = theme.darkGray
            color = theme.lightestColor
        }
    }

    val simpleInput by cssClass {
        display = "block"
        fontSize = theme.fontSize
        fontFamily = theme.fontFamily
        fontWeight = theme.fontWeight
        color = "#444"
        lineHeight = "1.3"
        padding = ".6em 1.4em .5em .8em"
        boxSizing = "border-box"
        margin = 0
        border = "1px solid #aaa"
        boxShadow = "0 1px 0 1px rgba(0,0,0,.04)"
        borderRadius = ".5em"
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        backgroundColor = "#fff"

        on(":hover") {
            borderColor = "#888"
        }

        on(":focus") {
            borderColor = "#aaa"
            boxShadow = "0 0 1px 3px rgba(59, 153, 252, .7)"
            // box-shadow: 0 0 0 3px -moz-mac-focusring;
            color = "#222"
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