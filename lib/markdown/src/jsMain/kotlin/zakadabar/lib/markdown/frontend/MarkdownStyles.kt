/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val markdownStyles by cssStyleSheet(MarkdownStyles())

class MarkdownStyles : ZkCssStyleSheet<ZkTheme>() {

    private fun withMarkdownTheme(func: (MarkdownTheme) -> Unit) {
        if (theme !is MarkdownThemeExt) return
        func((theme as MarkdownThemeExt).markdownTheme)
    }

    @Suppress("unused") // this is a CSS import, used by hljs
    val highlightStyles by cssImport {
        withMarkdownTheme {
            url = it.highlightUrl
        }
    }

    val content by cssClass {
        overflowX = "auto"
        padding = theme.layout.spacingStep * 2
        paddingTop = 0
    }

    val tocContainer by cssClass {
        boxSizing = "border-box"
        minWidth = 200 + theme.layout.spacingStep
        marginRight = theme.layout.spacingStep
        fontSize = "80%"
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
        paddingTop = 5
        paddingBottom = 5
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

    @Suppress("unused") // used implicitly by the browser
    val link by cssRule(".$content a") {
        color = theme.color.info
        textDecoration = "none"
    }

    @Suppress("unused") // used implicitly by the browser
    val header1 by cssRule(".$content h1") {
        marginTop = theme.layout.spacingStep * 2
    }

    @Suppress("unused") // used implicitly by the browser
    val header1First by cssRule(".$content h1:first-child") {
        marginTop = 0
    }

    @Suppress("unused") // used implicitly by the browser
    val header2 by cssRule(".$content h2") {
        marginTop = theme.layout.spacingStep * 2
    }

    @Suppress("unused") // used implicitly by the browser
    val header2First by cssRule(".$content h2:first-child") {
        marginTop = 0
    }

    @Suppress("unused") // used implicitly by the browser
    val header1Link by cssRule(".$content h1 > a") {
        paddingLeft = 20
        fontWeight = 400
        fontSize = "70%"
    }

    @Suppress("unused") // used implicitly by the browser
    val header2Link by cssRule(".$content h2 > a") {
        paddingLeft = 20
        fontWeight = 400
        fontSize = "70%"
    }

    @Suppress("unused") // used implicitly by the browser
    val img by cssRule(".$content img") {
        width = "100%"
    }

    val table by cssRule(".$content table") {
        border = "1px solid ${theme.color.border}"
        borderCollapse = "collapse"
        overflow = "auto"
        marginTop = 10
        marginBottom = 10
    }

    @Suppress("unused") // used implicitly by the browser
    val td by cssRule(".$content td") {
        border = "1px solid ${theme.color.border}"
        paddingTop = 4
        paddingBottom = 4
        paddingLeft = 8
        paddingRight = 8
    }

    @Suppress("unused") // used implicitly by the browser
    val inlineCode by cssRule(".$content code") {
        fontFamily = "JetBrains Mono, monospace"
        fontSize = 13
        paddingLeft = 4
        paddingRight = 4
        borderRadius = 2
        withMarkdownTheme { markdown ->
            markdown.backgroundColor?.let { backgroundColor = it }
        }
    }

    @Suppress("unused") // used implicitly by the browser
    val codeBlock by cssRule(".$content pre > code") {
        position = "relative"
        padding = 12
        lineHeight = 13 * 1.4
        marginLeft = - 12
        withMarkdownTheme { markdown ->
            markdown.borderColor?.let {
                borderLeft = "2px solid $it"
                borderTopLeftRadius = 0
                borderBottomLeftRadius = 0
            }
            markdown.backgroundColor?.let { backgroundColor = it }
        }
    }

    val codeCopy by cssClass {
        position = "absolute"
        right = 4
        top = 4
    }

    val codeCopyIcon by cssClass {
        cursor = "pointer"
        padding = 6
        borderRadius = 2

        hover {
            backgroundColor = theme.color.hoverBackground
        }
    }

    val codeCopySuccess by cssClass {
        fontFamily = theme.font.family
        borderRadius = 2
        alignSelf = "center"
        marginRight = 10
    }

    init {
        attach()
    }
}