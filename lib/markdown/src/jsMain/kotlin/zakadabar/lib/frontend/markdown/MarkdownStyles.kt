/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

val markdownStyles = MarkdownStyles()

class MarkdownStyles : ZkCssStyleSheet<ZkTheme>() {

    val content by cssClass {
        overflowX = "auto"
        padding = theme.layout.spacingStep * 2
        paddingTop = 0
    }

    val codeFence by cssClass {
        borderRadius = 4
        paddingLeft = 4
        paddingRight = 4
        backgroundColor = ZkColors.Gray.c300
        fontFamily = "monospace"
        fontWeight = "300"
    }

    val codeSpan by cssClass {
        borderRadius = 4
        paddingLeft = 4
        paddingRight = 4
        backgroundColor = ZkColors.Gray.c300
        fontFamily = "monospace"
        fontWeight = "300"
    }

    val inlineLink by cssClass {
        color = ZkColors.LightBlue.c700
    }

    val paragraph by cssClass {
        marginBottom = 10
    }

    val tocContainer by cssClass {
        boxSizing = "border-box"
        minWidth = 200 + theme.layout.spacingStep
        marginRight = theme.layout.spacingStep
        fontSize = "90%"
        paddingTop = theme.layout.spacingStep
    }

    val tocContent by cssClass {
        position = "absolute"
    }

    val tocEntry by cssClass {
        marginLeft = 1
        borderLeft = "1px solid ${theme.color.foreground}80"
        paddingLeft = theme.layout.spacingStep
        cursor = "pointer"
        paddingTop = 8
        paddingBottom = 8
        maxWidth = 200

        on("[data-active=\"true\"]") {
            marginLeft = 0
            paddingLeft = theme.layout.spacingStep - 1
            borderLeft = "3px solid ${theme.color.foreground}"
        }

        hover {
            backgroundColor = theme.color.hoverBackground
            color = theme.color.hoverForeground
        }
    }

    val tocText by cssClass {
        verticalAlign = "center"
    }

    val link by cssRule(".$content a") {
        color = theme.color.info
        textDecoration = "none"
    }

    init {
        attach()
    }

}