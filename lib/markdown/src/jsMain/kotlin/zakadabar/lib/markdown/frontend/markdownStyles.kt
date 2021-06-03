/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val markdownStyles by cssStyleSheet(MarkdownStyles())

open class MarkdownStyles : ZkCssStyleSheet() {

    open var codeBorderColor: String? = null
    open var highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/idea.min.css"

    init {
        hljs.registerLanguage("kotlin", hljsKotlin)
        hljs.registerLanguage("yaml", hljsYaml)
        hljs.registerLanguage("sql", hljsSql)
        hljs.registerLanguage("xml", hljsXml)
        hljs.registerLanguage("html", hljsXml)
    }

    @Suppress("unused") // this is a CSS import, used by hljs
    open val highlightStyles by cssImport {
        url = highlightUrl
    }

    val container by cssClass {
        display = "flex"
        flexDirection = "row"
        justifyContent = "center"
    }

    val content by cssClass {
        overflowX = "auto"
        overflowY = "hidden"
        maxWidth = 600
        flexGrow = 1
        margin = "auto"

//        small {
//            marginLeft = 0
//            marginRight = 0
//        }
//
//        medium {
//            marginLeft = theme.spacingStep / 2
//            marginRight = theme.spacingStep / 2
//        }
//
//        large {
//            marginLeft = theme.spacingStep
//            marginRight = theme.spacingStep
//        }
    }

    // -------------------------------------------------------------------------
    // Table of Contents
    // -------------------------------------------------------------------------

    open val tocContainer by cssClass {
        boxSizing = "border-box"
        marginRight = theme.spacingStep
        fontSize = "80%"
        paddingTop = theme.spacingStep / 2
        paddingBottom = theme.spacingStep / 2
        minWidth = 160 + theme.spacingStep
        display = "flex"
        flexDirection = "row"

        small {
            display = "none" // TODO markdown toc on small displays
        }

        medium {
            display = "none" // TODO markdown toc on medium displays
        }

    }

    open val tocContent by cssClass {
        position = "absolute"
        overflowY = "auto"
        overflowX = "hidden"
        flexGrow = 1
        styles["scrollbar-width"] = "none"
        on("::-webkit-scrollbar") {
            display = "none"
        }
    }

    open val tocEntry by cssClass {
        marginLeft = 1
        borderLeft = "1px solid ${theme.borderColor}"
        paddingLeft = theme.spacingStep
        cursor = "pointer"
        paddingTop = 5
        paddingBottom = 5
        width = 180

        on("[data-active=\"true\"]") {
            marginLeft = 0
            paddingLeft = theme.spacingStep - 1
            borderLeft = "3px solid ${theme.textColor}"
        }

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = theme.hoverTextColor
        }
    }

    open val tocText by cssClass {
        verticalAlign = "center"
    }

    // -------------------------------------------------------------------------
    // Content
    // -------------------------------------------------------------------------

    @Suppress("unused") // used implicitly by the browser
    open val link by cssRule(".$content a") {
        color = theme.infoColor
        textDecoration = "none"
    }

    @Suppress("unused") // used implicitly by the browser
    open val header1 by cssRule(".$content h1") {
        fontSize = 32
        fontWeight = 500
        borderBottom = theme.border
        marginBlockStart = theme.spacingStep * 2
        marginBlockEnd = theme.spacingStep
    }

    @Suppress("unused") // used implicitly by the browser
    open val header1First by cssRule(".$content h1:first-child") {
        marginBlockStart = theme.spacingStep
    }

    @Suppress("unused") // used implicitly by the browser
    open val header2 by cssRule(".$content h2") {
        marginBlockStart = theme.spacingStep * 2
        marginBlockEnd = theme.spacingStep / 2
        fontWeight = 500
    }

    @Suppress("unused") // used implicitly by the browser
    open val header3 by cssRule(".$content h3") {
        marginBlockStart = theme.spacingStep * 2
        marginBlockEnd = theme.spacingStep / 2
        fontWeight = 500
    }

    @Suppress("unused") // used implicitly by the browser
    open val header1Link by cssRule(".$content h1 > a") {
        paddingLeft = 20
        fontWeight = 400
        fontSize = "15px"
    }

    @Suppress("unused") // used implicitly by the browser
    open val header2Link by cssRule(".$content h2 > a") {
        paddingLeft = 20
        fontWeight = 400
        fontSize = "15px"
    }

    @Suppress("unused") // used implicitly by the browser
    open val header3Link by cssRule(".$content h3 > a") {
        paddingLeft = 20
        fontWeight = 400
        fontSize = "15px"
    }

    @Suppress("unused") // used implicitly by the browser
    open val img by cssRule(".$content img") {
        maxWidth = "100%"
    }

    open val li by cssRule(".$content li") {
        lineHeight = "1.5"
    }

    open val table by cssRule(".$content > table") {
        border = "1px solid ${theme.borderColor}"
        borderCollapse = "collapse"
        overflow = "auto"
        marginTop = 10
        marginBottom = 10
    }

    @Suppress("unused") // used implicitly by the browser
    open val td by cssRule(".$content > table td") {
        border = "1px solid ${theme.borderColor}"
        paddingTop = 4
        paddingBottom = 4
        paddingLeft = 8
        paddingRight = 8
    }

    @Suppress("unused") // used implicitly by the browser
    open val inlineCode by cssRule(".$content code") {
        fontFamily = "JetBrains Mono, monospace"
        fontSize = 13
        paddingLeft = 4
        paddingRight = 4
        borderRadius = 2
        backgroundColor = theme.blockBackgroundColor
    }

    @Suppress("unused") // used implicitly by the browser
    open val codeBlock by cssRule(".$content pre > code") {
        position = "relative"
        padding = 12
        paddingLeft = theme.spacingStep
        lineHeight = 13 * 1.4
        marginBottom = theme.spacingStep
        this@MarkdownStyles.codeBorderColor?.let {
            borderLeft = "2px solid $it"
            borderTopLeftRadius = 0
            borderBottomLeftRadius = 0
        }
        backgroundColor = theme.blockBackgroundColor
    }

    // -------------------------------------------------------------------------
    // Code Copy
    // -------------------------------------------------------------------------

    open val codeCopy by cssClass {
        position = "absolute"
        right = 0
        marginRight = 4
        top = 4
        background = theme.backgroundColor
        backgroundColor = theme.blockBackgroundColor
    }

    open val codeCopyIcon by cssClass {
        cursor = "pointer"
        padding = 6
        borderRadius = 2

        hover {
            backgroundColor = theme.hoverBackgroundColor
        }
    }

    // -------------------------------------------------------------------------
    // Util
    // -------------------------------------------------------------------------

    val unMarkdown by cssClass {
        on(" a") {
            color = "${theme.textColor} !important"
        }
    }

}