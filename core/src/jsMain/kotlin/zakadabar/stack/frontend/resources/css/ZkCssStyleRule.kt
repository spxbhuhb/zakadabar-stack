/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources.css

import zakadabar.stack.frontend.resources.ZkTheme

@Suppress("unused") // may be used by other modules
/**
 * One CSS rule. Puts properties of the rule into a map and then generates the CSS from
 * that map.
 *
 * The getter/setter pattern is intentional. With property delegation a new object would
 * be created for each property and we don't want that. Also, there are conversions,
 * so we can't use a simple map as delegate.
 */
class ZkCssStyleRule(
    private val sheet: ZkCssStyleSheet<*>,
    val propName: String,
    val cssClassName: String,
    var builder: ZkCssStyleRule.(ZkTheme) -> Unit
) {
    var media: String? = null
    var pseudoClass: String? = null

    val styles = mutableMapOf<String, String?>()

    private lateinit var variations: MutableList<ZkCssStyleRule>

    fun compile(): String {

        styles.clear()
        if (::variations.isInitialized) variations.clear()

        builder(sheet.theme)

        if (! ::variations.isInitialized) {
            return toCssString()
        }

        val strings = mutableListOf(toCssString())

        variations.forEach { strings += it.compile() }

        return strings.joinToString("\n")
    }

    private fun toCssString(): String {
        val styles = styles.map { style -> "    ${style.key}: ${style.value};" }.joinToString("\n")

        var s = ""

        if (media != null) s += "@media $media {\n"
        s += "."
        s += cssClassName
        if (pseudoClass != null) s += pseudoClass
        s += "{\n${styles}\n}"
        if (media != null) s += "}"

        return s
    }

    fun copyFrom(from: ZkCssStyleRule) {
        styles.clear()
        styles.putAll(from.styles)
    }

    // -------------------------------------------------------------------------
    // Pseudo-class and media methods
    // -------------------------------------------------------------------------

    fun hover(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(":hover", builder = builder)

    fun media(media: String, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = media, builder = builder)

    fun small(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = "(max-width: 600px)", builder = builder)

    fun medium(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = "(min-width: 800px)", builder = builder)

    fun large(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = "(min-width: 1200px)", builder = builder)

    fun on(pseudoClass: String? = null, media: String? = null, builder: ZkCssStyleRule.(ZkTheme) -> Unit) {
        require(pseudoClass != null || media != null) { "both pseudoClass and media is null" }

        if (! ::variations.isInitialized) variations = mutableListOf()

        val rule = ZkCssStyleRule(sheet, this.propName, cssClassName, builder)

        rule.pseudoClass = pseudoClass
        rule.media = media

        variations.add(rule)
    }

    // -------------------------------------------------------------------------
    // Build helpers
    // -------------------------------------------------------------------------

    /**
     * [MDN: align-items](https://developer.mozilla.org/en-US/docs/Web/CSS/align-items)
     *
     * ```
     *
     *   align-items <=> flex cross axis
     *
     *   flex-start
     *   flex-end
     *   center
     *   baseline
     *   stretch
     *
     * ```
     */
    var alignItems
        get() = styles["align-items"]
        set(value) {
            styles["align-items"] = value
        }

    /**
     * [MDN: align-items](https://developer.mozilla.org/en-US/docs/Web/CSS/align-self)
     *
     * ```
     *   align-self <=> flex cross axis
     *
     *    auto
     *    normal
     *
     *    center
     *    start
     *    end
     *    self-start
     *    self-end
     *    flex-start
     *    flex-end
     *
     *    baseline
     *    first baseline
     *    last baseline
     *    stretch
     *
     *    safe center
     *    unsafe center
     *
     * ```
     */
    var alignSelf
        get() = styles["align-self"]
        set(value) {
            styles["align-self"] = value
        }

    var appearance
        get() = styles["appearance"]
        set(value) {
            styles["appearance"] = value
        }

    var background
        get() = styles["background"]
        set(value) {
            styles["background"] = value
        }

    var backgroundColor
        get() = styles["background-color"]
        set(value) {
            styles["background-color"] = value
        }

    var backgroundImage
        get() = styles["background-image"]
        set(value) {
            styles["background-image"] = value
        }

    var backgroundPosition
        get() = styles["background-position"]
        set(value) {
            styles["background-position"] = value
        }

    var backgroundRepeat
        get() = styles["background-repeat"]
        set(value) {
            styles["background-repeat"] = value
        }

    var backgroundSize
        get() = styles["background-size"]
        set(value) {
            styles["background-size"] = value
        }

    var border: Any?
        get() = styles["border"]
        set(value) {
            styles["border"] = value.toString()
        }

    var borderColor
        get() = styles["border-color"]
        set(value) {
            styles["border-color"] = value
        }

    var borderCollapse
        get() = styles["border-collapse"]
        set(value) {
            styles["border-collapse"] = value
        }

    var borderTop: Any?
        get() = styles["border-top"]
        set(value) {
            styles["border-top"] = value.toString()
        }

    var borderRight: Any?
        get() = styles["border-right"]
        set(value) {
            styles["border-right"] = value.toString()
        }

    var borderBottom: Any?
        get() = styles["border-bottom"]
        set(value) {
            styles["border-bottom"] = value.toString()
        }

    var borderLeft: Any?
        get() = styles["border-left"]
        set(value) {
            styles["border-left"] = value.toString()
        }

    var borderRadius: Any?
        get() = styles["border-radius"]
        set(value) {
            styles["border-radius"] = stringOrPx(value)
        }

    var borderStyle
        get() = styles["border-style"]
        set(value) {
            styles["border-style"] = value
        }

    var borderWidth: Any?
        get() = styles["border-width"]
        set(value) {
            styles["border-width"] = stringOrPx(value)
        }

    var borderBottomWidth: Any?
        get() = styles["border-bottom-width"]
        set(value) {
            styles["border-bottom-width"] = stringOrPx(value)
        }

    var borderBottomLeftRadius: Any?
        get() = styles["border-bottom-left-radius"]
        set(value) {
            styles["border-bottom-left-radius"] = stringOrPx(value)
        }

    var borderTopLeftRadius: Any?
        get() = styles["border-top-left-radius"]
        set(value) {
            styles["border-top-left-radius"] = stringOrPx(value)
        }

    var borderBottomRightRadius: Any?
        get() = styles["border-bottom-right-radius"]
        set(value) {
            styles["border-bottom-right-radius"] = stringOrPx(value)
        }

    var borderTopRightRadius: Any?
        get() = styles["border-top-right-radius"]
        set(value) {
            styles["border-top-right-radius"] = stringOrPx(value)
        }

    /**
     * [MDN: box-sizing](https://developer.mozilla.org/en-US/docs/Web/CSS/box-sizing)
     *
     * ```
     *
     *   border-box
     *   content-box
     *
     * ```
     */
    var boxSizing
        get() = styles["box-sizing"]
        set(value) {
            styles["box-sizing"] = value
        }

    /**
     * [MDN: box-shadow](https://developer.mozilla.org/en-US/docs/Web/CSS/box-shadow)
     *
     * ```
     *
     * // Keyword values
     * box-shadow = "none"
     *
     * // offset-x | offset-y | color
     * box-shadow = "60px -16px teal"
     *
     * // offset-x | offset-y | blur-radius | color
     * box-shadow = "10px 5px 5px black"
     *
     * // offset-x | offset-y | blur-radius | spread-radius | color
     * box-shadow = "2px 2px 2px 1px rgba(0, 0, 0, 0.2)"
     *
     * // inset | offset-x | offset-y | color
     * box-shadow = "inset 5em 1em gold"
     *
     * // Any number of shadows, separated by commas
     * box-shadow = "3px 3px red, -1em 0 0.4em olive"
     *
     * ```
     */
    var boxShadow
        get() = styles["box-shadow"]
        set(value) {
            styles["box-shadow"] = value
        }

    var color
        get() = styles["color"]
        set(value) {
            styles["color"] = value
        }

    var cursor
        get() = styles["cursor"]
        set(value) {
            styles["cursor"] = value
        }

    var display
        get() = styles["display"]
        set(value) {
            styles["display"] = value
        }

    var fill
        get() = styles["fill"]
        set(value) {
            styles["fill"] = value
        }

    var flex
        get() = styles["flex"]
        set(value) {
            styles["flex"] = value
        }

    var flexDirection
        get() = styles["flex-direction"]
        set(value) {
            styles["flex-direction"] = value
        }

    var flexGrow: Any?
        get() = styles["flex-grow"]
        set(value) {
            styles["flex-grow"] = value.toString()
        }

    var flexWrap
        get() = styles["flex-wrap"]
        set(value) {
            styles["flex-wrap"] = value
        }

    var fontFamily
        get() = styles["font-family"]
        set(value) {
            styles["font-family"] = value
        }

    var fontSize: Any?
        get() = styles["font-size"]
        set(value) {
            styles["font-size"] = stringOrPx(value)
        }

    var fontWeight: Any?
        get() = styles["font-weight"]
        set(value) {
            styles["font-weight"] = value.toString()
        }

    var fontStyle
        get() = styles["font-style"]
        set(value) {
            styles["font-style"] = value
        }

    var fontVariant
        get() = styles["font-variant"]
        set(value) {
            styles["font-varient"] = value
        }

    var fontStretch
        get() = styles["font-stretch"]
        set(value) {
            styles["font-stretch"] = value
        }

    var gap: Any?
        get() = styles["gap"]
        set(value) {
            styles["gap"] = stringOrPx(value)
        }

    var gridColumnEnd: Int?
        get() = styles["grid-column-end"]?.toInt()
        set(value) {
            styles["grid-column-end"] = value.toString()
        }

    var gridColumn
        get() = styles["grid-column"]
        set(value) {
            styles["grid-column"] = value
        }

    var gridColumnStart: Int?
        get() = styles["grid-column-start"]?.toInt()
        set(value) {
            styles["grid-column-start"] = value.toString()
        }

    var gridRowEnd: Int?
        get() = styles["grid-row-end"]?.toInt()
        set(value) {
            styles["grid-row-end"] = value.toString()
        }

    var gridRowStart: Int?
        get() = styles["grid-row-start"]?.toInt()
        set(value) {
            styles["grid-row-start"] = value.toString()
        }

    var gridTemplateColumns: String?
        get() = styles["grid-template-columns"]
        set(value) {
            styles["grid-template-columns"] = value
        }

    var gridTemplateRows: String?
        get() = styles["grid-template-rows"]
        set(value) {
            styles["grid-template-rows"] = value
        }

    var height: Any?
        get() = styles["height"]
        set(value) {
            styles["height"] = stringOrPx(value)
        }

    /**
     * [MDN: justify-content](https://developer.mozilla.org/en-US/docs/Web/CSS/justify-content)
     *
     * ```
     *
     *   justify-content <=> flex main axis
     *
     *   flex-start
     *   flex-end
     *   center
     *   space-between
     *   space-around
     *   space-evenly
     *
     * ```
     */
    var justifyContent
        get() = styles["justify-content"]
        set(value) {
            styles["justify-content"] = value
        }

    var lineHeight: Any?
        get() = styles["line-height"]
        set(value) {
            styles["line-height"] = stringOrPx(value)
        }

    var margin: Any?
        get() = styles["margin"]
        set(value) {
            styles["margin"] = stringOrPx(value)
        }

    var marginBottom: Any?
        get() = styles["margin-bottom"]
        set(value) {
            styles["margin-bottom"] = stringOrPx(value)
        }

    var marginTop: Any?
        get() = styles["margin-top"]
        set(value) {
            styles["margin-top"] = stringOrPx(value)
        }

    var marginLeft
        get() = styles["margin-left"]?.toInt()
        set(value) {
            styles["margin-left"] = stringOrPx(value)
        }

    var marginRight: Any?
        get() = styles["margin-right"]
        set(value) {
            styles["margin-right"] = stringOrPx(value)
        }

    var maxHeight: Any?
        get() = styles["max-height"]
        set(value) {
            styles["max-height"] = stringOrPx(value)
        }

    var maxWidth: Any?
        get() = styles["max-width"]
        set(value) {
            styles["max-width"] = stringOrPx(value)
        }

    var minHeight: Any?
        get() = styles["min-height"]
        set(value) {
            styles["min-height"] = stringOrPx(value)
        }

    var mozAppearance
        get() = styles["-moz-appearance"]
        set(value) {
            styles["-moz-appearance"] = value
        }

    var minWidth: Any?
        get() = styles["min-width"]
        set(value) {
            styles["min-width"] = stringOrPx(value)
        }

    var opacity: Any?
        get() = styles["opacity"]
        set(value) {
            styles["opacity"] = value.toString()
        }

    var outline
        get() = styles["outline"]
        set(value) {
            styles["outline"] = value
        }

    var overflow
        get() = styles["overflow"]
        set(value) {
            styles["overflow"] = value
        }

    var overflowX
        get() = styles["overflow-x"]
        set(value) {
            styles["overflow-x"] = value
        }

    var overflowY
        get() = styles["overflow-y"]
        set(value) {
            styles["overflow-y"] = value
        }

    var padding: Any?
        get() = styles["padding"]
        set(value) {
            styles["padding"] = stringOrPx(value)
        }

    var paddingBottom: Any?
        get() = styles["padding-bottom"]
        set(value) {
            styles["padding-bottom"] = stringOrPx(value)
        }

    var paddingTop: Any?
        get() = styles["padding-top"]
        set(value) {
            styles["padding-top"] = stringOrPx(value)
        }

    var paddingLeft: Any?
        get() = styles["padding-left"]
        set(value) {
            styles["padding-left"] = stringOrPx(value)
        }

    var paddingRight: Any?
        get() = styles["padding-right"]
        set(value) {
            styles["padding-right"] = stringOrPx(value)
        }

    var pointerEvents
        get() = styles["pointer-events"]
        set(value) {
            styles["pointer-events"] = value
        }

    var position
        get() = styles["position"]
        set(value) {
            styles["position"] = value
        }

    var resize
        get() = styles["resize"]
        set(value) {
            styles["resize"] = value
        }

    var strokeWidth: Any?
        get() = styles["stroke-width"]
        set(value) {
            styles["stroke-width"] = stringOrPx(value)
        }

    var textAlign
        get() = styles["text-align"]
        set(value) {
            styles["text-align"] = value
        }

    var textDecoration
        get() = styles["text-decoration"]
        set(value) {
            styles["text-decoration"] = value
        }

    var textOverflow
        get() = styles["text-overflow"]
        set(value) {
            styles["text-overflow"] = value
        }

    var textTransform
        get() = styles["text-transform"]
        set(value) {
            styles["text-transform"] = value
        }

    var top: Any?
        get() = styles["top"]
        set(value) {
            styles["top"] = stringOrPx(value)
        }

    var right: Any?
        get() = styles["right"]
        set(value) {
            styles["right"] = stringOrPx(value)
        }

    var bottom: Any?
        get() = styles["bottom"]
        set(value) {
            styles["bottom"] = stringOrPx(value)
        }

    var left: Any?
        get() = styles["left"]
        set(value) {
            styles["left"] = stringOrPx(value)
        }

    /**
     * [MDN: user-select](https://developer.mozilla.org/en-US/docs/Web/CSS/user-select)
     *
     * ```
     *   // Keyword values
     *   user-select: none;
     *   user-select: auto;
     *   user-select: text;
     *   user-select: contain;
     *   user-select: all;
     *
     *   // Global values
     *   user-select: inherit;
     *   user-select: initial;
     *   user-select: unset;
     * ```
     */
    var userSelect
        get() = styles["user-select"]
        set(value) {
            styles["user-select"] = value
        }

    var verticalAlign
        get() = styles["vertical-align"]
        set(value) {
            styles["vertical-align"] = value
        }

    var webkitAppearance
        get() = styles["-webkit-appearance"]
        set(value) {
            styles["-webkit-appearance"] = value
        }

    /**
     * [MDN: white-space](https://developer.mozilla.org/en-US/docs/Web/CSS/white-space)
     *
     * ```
     *
     * white-space: normal;
     * white-space: nowrap;
     * white-space: pre;
     * white-space: pre-wrap;
     * white-space: pre-line;
     * white-space: break-spaces;
     * ```
     */
    var whiteSpace
        get() = styles["white-space"]
        set(value) {
            styles["white-space"] = value
        }

    var width: Any?
        get() = styles["width"]
        set(value) {
            styles["width"] = stringOrPx(value)
        }

    var zIndex: Any?
        get() = styles["z-index"]
        set(value) {
            styles["z-index"] = value.toString()
        }
}