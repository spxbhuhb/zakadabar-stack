/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource.css

import zakadabar.core.resource.ZkTheme
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("unused") // may be used by other modules
/**
 * One CSS rule. Puts properties of the rule into a map and then generates the CSS from
 * that map.
 *
 * The getter/setter pattern is intentional. With property delegation a new object would
 * be created for each property and we don't want that. Also, there are conversions,
 * so we can't use a simple map as delegate.
 *
 * @property  sheet          The CSS Style Sheet this rule is part of.
 * @property  propName       Name of the property in the CSS style sheet.
 * @property  cssClassname   Name of the CSS class to use.
 * @property  cssSelector    The CSS selector to include in the style sheet. When [cssSelector]
 *                           is not null [cssClassName] is skipped. Using [on] for a rule
 *                           with [cssSelector] does not work.
 * @property  builder        The function to build the CSS text for the rule.
 */
class ZkCssStyleRule(
    private val sheet: ZkCssStyleSheet,
    val propName: String,
    val cssClassname: String,
    val cssSelector: String? = null,
    var builder: ZkCssStyleRule.(ZkTheme) -> Unit
) : ReadOnlyProperty<ZkCssStyleSheet, ZkCssStyleRule> {
    var media: String? = null
    var pseudoClass: String? = null

    val styles = mutableMapOf<String, String?>()

    private lateinit var variations: MutableList<ZkCssStyleRule>

    override fun getValue(thisRef: ZkCssStyleSheet, property: KProperty<*>) = this

    override fun toString() = cssClassname

    operator fun CssValueConst.unaryPlus() {
        styles[name] = value
    }

    // -------------------------------------------------------------------------
    // Pseudo-class and media methods
    // -------------------------------------------------------------------------

    fun hover(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(":hover", builder = builder)

    fun firstChild(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(":first-child", builder = builder)

    fun lastChild(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(":last-child", builder = builder)

    fun media(media: String, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = media, builder = builder)

    /**
     * Applies the style on screens that are less then 600px wide.
     */
    fun small(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = "(max-width: 600px)", builder = builder)

    /**
     * Applies the style on screens that are less then 800px wide.
     */
    fun medium(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = "(max-width: 800px)", builder = builder)

    /**
     * Applies the style on screens that are more then 800px wide.
     */
    fun large(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = on(media = "(min-width: 800px)", builder = builder)

    fun on(pseudoClass: String? = null, media: String? = null, builder: ZkCssStyleRule.(ZkTheme) -> Unit) {
        require(pseudoClass != null || media != null) { "both pseudoClass and media is null" }

        if (! ::variations.isInitialized) variations = mutableListOf()

        val rule = ZkCssStyleRule(sheet, this.propName, cssClassname, cssSelector, builder)

        rule.pseudoClass = pseudoClass
        rule.media = media

        variations.add(rule)
    }

    // -------------------------------------------------------------------------
    // Compilation
    // -------------------------------------------------------------------------

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
        val styleStrings = styles.map { style -> "    ${style.key}: ${style.value};" }.joinToString("\n")

        var s = ""

        when (cssSelector) {
            null -> {
                if (media != null) s += "@media $media {\n"
                s += "."
                s += cssClassname
                if (pseudoClass != null) s += pseudoClass
            }
            "@import" -> {
                val url = styles["url"] ?: return ""
                return "@import url(\"${url}\");"
            }
            else -> {
                s += cssSelector
            }
        }

        s += "{\n${styleStrings}\n}"

        if (media != null) s +=
            "}"

        return s
    }

    fun copyFrom(from: ZkCssStyleRule) {
        styles.clear()
        styles.putAll(from.styles)
    }

    // -------------------------------------------------------------------------
    // Build helpers
    // -------------------------------------------------------------------------

    class CssValueDelegate : ReadWriteProperty<ZkCssStyleRule, String> {
        override fun getValue(thisRef: ZkCssStyleRule, property: KProperty<*>): String {
            TODO("Not yet implemented")
        }

        override fun setValue(thisRef: ZkCssStyleRule, property: KProperty<*>, value: String) {
            TODO("Not yet implemented")
        }

    }

    /**
     * [MDN: align-items](https://developer.mozilla.org/en-US/docs/Web/CSS/align-items)
     *
     * align-items <=> flex cross axis
     */
    var alignItems
        get() = styles["align-items"]
        set(value) {
            styles["align-items"] = value
        }

    /**
     * [MDN: align-self](https://developer.mozilla.org/en-US/docs/Web/CSS/align-self)
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

    var border
        get() = styles["border"]
        set(value) {
            styles["border"] = value.toString()
        }

    var borderColor
        get() = styles["border-color"]
        set(value) {
            styles["border-color"] = value
        }

    /**
     * [MDN: border-collapse](https://developer.mozilla.org/en-US/docs/Web/CSS/border-collapse)
     *
     * ```
     *
     *    collapse
     *    separate
     *
     * ```
     */
    var borderCollapse
        get() = styles["border-collapse"]
        set(value) {
            styles["border-collapse"] = value
        }

    var borderTop
        get() = styles["border-top"]
        set(value) {
            styles["border-top"] = value
        }

    var borderRight
        get() = styles["border-right"]
        set(value) {
            styles["border-right"] = value
        }

    var borderBottom
        get() = styles["border-bottom"]
        set(value) {
            styles["border-bottom"] = value
        }

    var borderLeft
        get() = styles["border-left"]
        set(value) {
            styles["border-left"] = value
        }

    var borderRadius: String?
        get() = styles["border-radius"]
        set(value) {
            styles["border-radius"] = value
        }

    var borderStyle
        get() = styles["border-style"]
        set(value) {
            styles["border-style"] = value
        }

    var borderWidth
        get() = styles["border-width"]
        set(value) {
            styles["border-width"] = value
        }

    var borderBottomWidth
        get() = styles["border-bottom-width"]
        set(value) {
            styles["border-bottom-width"] = value
        }

    var borderBottomLeftRadius
        get() = styles["border-bottom-left-radius"]
        set(value) {
            styles["border-bottom-left-radius"] = value
        }

    var borderTopLeftRadius
        get() = styles["border-top-left-radius"]
        set(value) {
            styles["border-top-left-radius"] = value
        }

    var borderBottomRightRadius
        get() = styles["border-bottom-right-radius"]
        set(value) {
            styles["border-bottom-right-radius"] = value
        }

    var borderTopRightRadius
        get() = styles["border-top-right-radius"]
        set(value) {
            styles["border-top-right-radius"] = value
        }

    /**
     * [MDN: box-sizing](https://developer.mozilla.org/en-US/docs/Web/CSS/box-sizing)
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

    var content
        get() = styles["content"]
        set(value) {
            styles["content"] = value
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

    var flexGrow : Double?
        get() = styles["flex-grow"]?.toDouble()
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

    var fontSize
        get() = styles["font-size"]
        set(value) {
            styles["font-size"] = value
        }

    var fontWeight
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

    var gap
        get() = styles["gap"]
        set(value) {
            styles["gap"] = value
        }

    var gridAutoColumns
        get() = styles["grid-auto-columns"]
        set(value) {
            styles["grid-auto-columns"] = value
        }

    var gridAutoRows
        get() = styles["grid-auto-rows"]
        set(value) {
            styles["grid-auto-rows"] = value
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

    var gridGap
        get() = styles["grid-gap"]
        set(value) {
            styles["grid-gap"] = value
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

    var height
        get() = styles["height"]
        set(value) {
            styles["height"] = value
        }

    /**
     * [MDN: justify-content](https://developer.mozilla.org/en-US/docs/Web/CSS/justify-content)
     *
     * justify-content <=> flex main axis
     */
    var justifyContent
        get() = styles["justify-content"]
        set(value) {
            styles["justify-content"] = value
        }

    /**
     * [MDN: justify-self](https://developer.mozilla.org/en-US/docs/Web/CSS/justify-self)
     *
     * ```
     *
     *   justify-self <=> flex main axis
     *
     *   auto
     *   normal
     *   stretch
     *
     *   -- Positional alignment
     *
     *   center     -- Pack item around the center
     *   start      -- Pack item from the start
     *   end        -- Pack item from the end
     *   flex-start -- Equivalent to 'start'. Note that justify-self is ignored in Flexbox layouts.
     *   flex-end   -- Equivalent to 'end'. Note that justify-self is ignored in Flexbox layouts.
     *   self-start
     *   self-end
     *   left       -- Pack item from the left
     *   right      -- Pack item from the right
     *
     *  -- Baseline alignment
     *   baseline
     *   first baseline
     *   last baseline
     *
     *  -- Overflow alignment (for positional alignment only)
     *   safe center
     *   unsafe center
     *
     * -- Global values
     *   inherit
     *   initial
     *   unset
     *
     * ```
     */
    var justifySelf
        get() = styles["justify-self"]
        set(value) {
            styles["justify-self"] = value
        }

    var lineHeight
        get() = styles["line-height"]
        set(value) {
            styles["line-height"] = value
        }

    var margin
        get() = styles["margin"]
        set(value) {
            styles["margin"] = value
        }

    var marginBlockStart
        get() = styles["margin-block-start"]
        set(value) {
            styles["margin-block-start"] = value
        }

    var marginBlockEnd
        get() = styles["margin-block-end"]
        set(value) {
            styles["margin-block-end"] = value
        }

    var marginBottom
        get() = styles["margin-bottom"]
        set(value) {
            styles["margin-bottom"] = value
        }

    var marginTop
        get() = styles["margin-top"]
        set(value) {
            styles["margin-top"] = value
        }

    var marginLeft
        get() = styles["margin-left"]
        set(value) {
            styles["margin-left"] = value
        }

    var marginRight
        get() = styles["margin-right"]
        set(value) {
            styles["margin-right"] = value
        }

    var maxHeight
        get() = styles["max-height"]
        set(value) {
            styles["max-height"] = value
        }

    var maxWidth
        get() = styles["max-width"]
        set(value) {
            styles["max-width"] = value
        }

    var minHeight
        get() = styles["min-height"]
        set(value) {
            styles["min-height"] = value
        }

    var mozAppearance
        get() = styles["-moz-appearance"]
        set(value) {
            styles["-moz-appearance"] = value
        }

    var minWidth
        get() = styles["min-width"]
        set(value) {
            styles["min-width"] = value
        }

    var opacity
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

    var padding
        get() = styles["padding"]
        set(value) {
            styles["padding"] = value
        }

    var paddingBottom
        get() = styles["padding-bottom"]
        set(value) {
            styles["padding-bottom"] = value
        }

    var paddingTop
        get() = styles["padding-top"]
        set(value) {
            styles["padding-top"] = value
        }

    var paddingLeft
        get() = styles["padding-left"]
        set(value) {
            styles["padding-left"] = value
        }

    var paddingRight
        get() = styles["padding-right"]
        set(value) {
            styles["padding-right"] = value
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

    var strokeWidth
        get() = styles["stroke-width"]
        set(value) {
            styles["stroke-width"] = value
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

    /**
     * [MDN: text-transform](https://developer.mozilla.org/en-US/docs/Web/CSS/text-transform)
     *
     * ```
     *
     *    none
     *    capitalize
     *    uppercase
     *    lowercase
     *    full-width
     *    full-size-kana
     *
     * ```
     */
    var textTransform
        get() = styles["text-transform"]
        set(value) {
            styles["text-transform"] = value
        }

    var top
        get() = styles["top"]
        set(value) {
            styles["top"] = value
        }

    var right
        get() = styles["right"]
        set(value) {
            styles["right"] = value
        }

    var bottom
        get() = styles["bottom"]
        set(value) {
            styles["bottom"] = value
        }

    var left
        get() = styles["left"]
        set(value) {
            styles["left"] = value
        }

    var transform
        get() = styles["transform"]
        set(value) {
            styles["transform"] = value
        }

    var transition
        get() = styles["transition"]
        set(value) {
            styles["transition"] = value
        }

    /**
     * [MDN: import](https://developer.mozilla.org/en-US/docs/Web/CSS/@import)
     *
     * URL of the imported style sheet.
     */
    var url
        get() = styles["url"]
        set(value) {
            styles["url"] = value
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

    var webkitTransform
        get() = styles["-webkit-transform"]
        set(value) {
            styles["-webkit-transform"] = value
        }

    var webkitTransition
        get() = styles["-webkit-transition"]
        set(value) {
            styles["-webkit-transition"] = value
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

    var width
        get() = styles["width"]
        set(value) {
            styles["width"] = value
        }

    var zIndex
        get() = styles["z-index"]
        set(value) {
            styles["z-index"] = value.toString()
        }

}