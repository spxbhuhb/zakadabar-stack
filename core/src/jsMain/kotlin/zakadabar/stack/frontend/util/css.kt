/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import kotlinx.atomicfu.atomic
import kotlinx.browser.document
import zakadabar.stack.util.PublicApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

var defaultTheme = Theme()

open class Theme {

    open var darkestColor = "#0d5b28"
    open var darkColor = "#2e8d36"
    open var lightColor = "#43cd50"
    open var lightestColor = "#fff"
    open var darkestGray = "#7b7b7b"
    open var darkGray = "#acabab"
    open var gray = "#d9d9d9"
    open var lightGray = "#f5f5f5"

    open var infoColor = "#6f90e5"
    open var errorColor = "#D71313"
    open var approveColor = darkColor
    open var cancelColor = "#bfbe96"
    open var selectedColor = "#486cc7"

    open var headerBackground = "rgba(13,91,40,0.05)"
    open var headerForeground = "#0d5b28"
    open var headerIconBackground = "0d5b28"
    open var headerIconFill = "$43cd50"
    open var headerToolBackground = "rgba(13,91,40,0.05)"
    open var headerToolFill = "#2e8d36"

    open var sliderColor = lightGray
    open var fontFamily = "IBM Plex Sans"
    open var fontSize = 12

    open var borderRadius = 2

    open var margin = 8

    open var contentWidth = 600
    open var headerHeight = 26
}

typealias RuleInit = CssStyleRule.(Theme) -> Unit

open class CssStyleSheet<T : CssStyleSheet<T>>(val theme: Theme) {

    companion object {
        val nextId = atomic(0)
    }

    val id = nextId.getAndIncrement()
    val element = document.createElement("style")

    init {
        element.id = "zkc-$id"
    }

    internal val rules = mutableMapOf<String, CssStyleRule>()

    fun cssClass(init: RuleInit): ReadOnlyProperty<CssStyleSheet<T>, String> =
        object : ReadOnlyProperty<CssStyleSheet<T>, String> {

            val cssClassName = "zkc-${nextId.getAndIncrement()}"
            val rule = CssStyleRule(this@CssStyleSheet, cssClassName)

            init {
                rule.init(theme)
                rules[cssClassName] = rule
            }

            override fun getValue(thisRef: CssStyleSheet<T>, property: KProperty<*>) = cssClassName
        }


    @PublicApi
    fun attach(): T {
        element.innerHTML = rules.map { rule ->
            "." + rule.key + " {\n" + rule.value.styles.map { "${it.key}: ${it.value};" }.joinToString("\n") + "}"
        }.joinToString("\n")

        document.body?.appendChild(element)

        @Suppress("UNCHECKED_CAST") // returns with itself, should be OK
        return this as T
    }

    @PublicApi
    fun detach() {
        element.remove()
    }

}

@Suppress("unused") // may be used by other modules
class CssStyleRule(val sheet: CssStyleSheet<*>, val cssClassName: String) {

    val styles = mutableMapOf<String, String?>()

    private fun stringOrPx(value: Any?) = if (value is String) value else "${value}px"

    fun on(pseudoClass: String, init: RuleInit) {
        val rule = CssStyleRule(sheet, cssClassName)
        rule.init(sheet.theme)
        sheet.rules[cssClassName + pseudoClass] = rule
    }

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

    var gridColumnEnd: Int?
        get() = styles["grid-column-end"]?.toInt()
        set(value) {
            styles["grid-column-end"] = value.toString()
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

    var minWidth: Any?
        get() = styles["min-width"]
        set(value) {
            styles["min-width"] = stringOrPx(value)
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