/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.browser

import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

val markdownStyles by cssStyleSheet(MarkdownStyles())

open class MarkdownStyles : ZkCssStyleSheet() {

    open var codeBorderColor: String? = null
    open var highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/idea.min.css"

    open var defaultWidth by cssParameter { 800 }

    init {
        hljs.registerLanguage("kotlin", hljsKotlin)
        hljs.registerLanguage("yaml", hljsYaml)
        hljs.registerLanguage("sql", hljsSql)
        hljs.registerLanguage("xml", hljsXml)
        hljs.registerLanguage("html", hljsXml)
        hljs.registerLanguage("text", hljsText)
        hljs.registerLanguage("shell", hljsText)
    }

    @Suppress("unused") // this is a CSS import, used by hljs
    open val highlightStyles by cssImport {
        url = highlightUrl
    }

    val container by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.center
        maxWidth = 100.percent
    }

    val content by cssClass {
        + OverflowX.auto
        + OverflowY.hidden

        //maxWidth = 600.px
        flexGrow = 1.0
        margin = "auto"
        maxWidth = defaultWidth.px

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
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.row

        minWidth = (160 + theme.spacingStep).px

        marginRight = theme.spacingStep.px
        paddingTop = (theme.spacingStep / 2).px
        paddingBottom = (theme.spacingStep / 2).px

        fontSize = 12.px

        small {
            + Display.none // TODO markdown toc on small displays
        }

        medium {
            + Display.none // TODO markdown toc on medium displays
        }

    }

    open val tocContent by cssClass {
        + Position.absolute
        + OverflowY.auto
        + OverflowX.hidden

        flexGrow = 1.0

        styles["scrollbar-width"] = "none"
        on("::-webkit-scrollbar") {
            + Display.none
        }
    }

    open val tocEntry by cssClass {
        + Cursor.pointer

        marginLeft = 1.px
        borderLeft = "1px solid ${theme.borderColor}"
        paddingLeft = theme.spacingStep.px
        paddingTop = 5.px
        paddingBottom = 5.px
        width = 180.px

        on("[data-active=\"true\"]") {
            marginLeft = 0.px
            paddingLeft = (theme.spacingStep - 1).px
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
    open val p by cssRule(".$content p") {
        maxWidth = defaultWidth.px
        marginLeft = "auto"
        marginRight = "auto"
    }

    @Suppress("unused") // used implicitly by the browser
    open val link by cssRule(".$content a") {
        color = theme.infoColor
        textDecoration = "none"
    }

    @Suppress("unused") // used implicitly by the browser
    open val header1 by cssRule(".$content h1") {
        maxWidth = defaultWidth.px
        margin = "auto"
        fontSize = 32.px
        fontWeight = 500.weight
        borderBottom = theme.border
        marginBlockStart = (theme.spacingStep * 2).px
        marginBlockEnd = theme.spacingStep.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val header1First by cssRule(".$content h1:first-child") {
        marginBlockStart = theme.spacingStep.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val h2 by cssRule(".$content h2") {
        maxWidth = defaultWidth.px
        margin = "auto"
        marginBlockStart = (theme.spacingStep * 2).px
        marginBlockEnd = (theme.spacingStep / 2).px
        fontWeight = 500.weight
    }

    @Suppress("unused") // used implicitly by the browser
    open val h3 by cssRule(".$content h3") {
        maxWidth = defaultWidth.px
        margin = "auto"
        marginBlockStart = (theme.spacingStep * 2).px
        marginBlockEnd = (theme.spacingStep / 2).px
        fontWeight = 500.weight
    }

    @Suppress("unused") // used implicitly by the browser
    open val h4 by cssRule(".$content h4") {
        maxWidth = defaultWidth.px
        margin = "auto"
    }

    @Suppress("unused") // used implicitly by the browser
    open val h5 by cssRule(".$content h5") {
        maxWidth = defaultWidth.px
        margin = "auto"
    }

    @Suppress("unused") // used implicitly by the browser
    open val h6 by cssRule(".$content h6") {
        maxWidth = defaultWidth.px
        margin = "auto"
    }

    @Suppress("unused") // used implicitly by the browser
    open val ul by cssRule(".$content ul") {
        maxWidth = defaultWidth.px
        margin = "auto"
        marginBlockEnd = theme.spacingStep.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val ol by cssRule(".$content ol") {
        maxWidth = defaultWidth.px
        margin = "auto"
        marginBlockEnd = theme.spacingStep.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val header1Link by cssRule(".$content h1 > a") {
        paddingLeft = 20.px
        fontWeight = 400.weight
        fontSize = 15.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val header2Link by cssRule(".$content h2 > a") {
        paddingLeft = 20.px
        fontWeight = 400.weight
        fontSize = 15.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val header3Link by cssRule(".$content h3 > a") {
        paddingLeft = 20.px
        fontWeight = 400.weight
        fontSize = 15.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val img by cssRule(".$content img") {
        maxWidth = 100.percent
    }

    @Suppress("unused") // used implicitly by the browser
    open val li by cssRule(".$content li") {
        lineHeight = "1.5"
    }

    open val table by cssRule(".$content > table") {
        border = "1px solid ${theme.borderColor}"
        borderCollapse = "collapse"
        overflow = "auto"
        marginTop = 10.px
        marginBottom = 10.px
        maxWidth = 90.percent
        marginLeft = "auto"
        marginRight = "auto"
    }

    @Suppress("unused") // used implicitly by the browser
    open val td by cssRule(".$content > table td") {
        border = "1px solid ${theme.borderColor}"
        paddingTop = 4.px
        paddingBottom = 4.px
        paddingLeft = 8.px
        paddingRight = 8.px
    }

    @Suppress("unused") // used implicitly by the browser
    open val inlineCode by cssRule(".$content code") {
        fontFamily = "JetBrains Mono, monospace"
        fontSize = 13.px
        paddingLeft = 4.px
        paddingRight = 4.px
        borderRadius = 2.px
        backgroundColor = theme.blockBackgroundColor
    }

    @Suppress("unused") // used implicitly by the browser
    open val codeBlockContainer by cssRule(".$content pre") {
        + Position.relative
        + BoxSizing.borderBox
        paddingRight = 34.px
        width = 100.percent
    }

    @Suppress("unused") // used implicitly by the browser
    open val codeBlock by cssRule(".$content pre > code") {
        + BoxSizing.borderBox
        padding = 12.px
        paddingLeft = theme.spacingStep.px
        lineHeight = (13 * 1.4).px
        marginBottom = theme.spacingStep.px
//        this@MarkdownStyles.codeBorderColor?.let {
//            borderLeft = "2px solid $it"
//            borderTopLeftRadius = 0.px
//            borderBottomLeftRadius = 0.px
//        }
        borderRadius = Borders.borderRadius.md
        boxShadow = BoxShadows.md
        backgroundColor = theme.blockBackgroundColor
        width = 100.percent
        marginRight = 0.px
    }

    // -------------------------------------------------------------------------
    // Code Copy
    // -------------------------------------------------------------------------

    open val codeCopy by cssClass {
        + Position.absolute
        right = 0.px
        top = 0.px
        background = theme.backgroundColor
        backgroundColor = theme.blockBackgroundColor
        boxShadow = BoxShadows.md
        borderRadius = Borders.borderRadius.md
    }

    open val codeCopyIcon by cssClass {
        + Cursor.pointer
        padding = 6.px
        borderRadius = Borders.borderRadius.md
        color = theme.textColor
        fill = theme.textColor
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

    val enrichElement by cssClass {
        maxWidth = 600.px
        margin = "auto"
        marginBlockStart = 20.px
    }

}