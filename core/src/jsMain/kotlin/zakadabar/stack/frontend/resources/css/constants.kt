/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.stack.frontend.resources.css

object BoxSizing {
    val borderBox = CssValueConst("box-sizing", "border-box")
    val contentBox = CssValueConst("box-sizing", "content-box")
}

object Display {
    val block = CssValueConst("display", "block")
    val inline = CssValueConst("display", "inline")
    val inlineBlock = CssValueConst("display", "inline-block")
    val flex = CssValueConst("display", "flex")
    val inlineFlex = CssValueConst("display", "inline-flex")
    val grid = CssValueConst("display", "grid")
    val inlineGrid = CssValueConst("display", "inline-grid")
    val flowRoot = CssValueConst("display", "flow-root")
    val none = CssValueConst("display", "none")
    val contents = CssValueConst("display", "contents")
    val blockFlow = CssValueConst("display", "block flow")
    val inlineFlow = CssValueConst("display", "inline flow")
    val inlineFlowRoot = CssValueConst("display", "inline flow-root")
    val blockFlex = CssValueConst("display", "block flex")
    val blockGrid = CssValueConst("display", "block grid")
    val blockFlowRoot = CssValueConst("display", "block flow-root")
    val table = CssValueConst("display", "table")
    val tableRow = CssValueConst("display", "table-row")
    val listItem = CssValueConst("display", "list-item")
}

object FlexDirection {
    val column = CssValueConst("flex-direction", "column")
    val row = CssValueConst("flex-direction", "row")
}

object AlignItems {
    val flexStart = CssValueConst("align-items", "flex-start")
    val flexEnd = CssValueConst("align-items", "flex-end")
    val center = CssValueConst("align-items", "center")
    val baseLine = CssValueConst("align-items", "baseline")
    val stretch = CssValueConst("align-items", "stretch")
}

object AlignSelf {
    val auto = CssValueConst("align-self", "auto")
    val normal = CssValueConst("align-self", "normal")
    val center = CssValueConst("align-self", "center")
    val start = CssValueConst("align-self", "start")
    val end = CssValueConst("align-self", "end")
    val selfStart = CssValueConst("align-self", "self-start")
    val selfEnd = CssValueConst("align-self", "self-end")
    val flexStart = CssValueConst("align-self", "flex-start")
    val flexEnd = CssValueConst("align-self", "flex-end")
    val baseline = CssValueConst("align-self", "baseline")
    val firstBaseline = CssValueConst("align-self", "first baseline")
    val lastBaseline = CssValueConst("align-self", "last baseline")
    val stretch = CssValueConst("align-self", "stretch")
    val safeCenter = CssValueConst("align-self", "safe center")
    val unsafeCenter = CssValueConst("align-self", "unsafe center")
}

object JustifyContent {
    val flexStart = CssValueConst("justify-content", "flex-start")
    val flexEnd = CssValueConst("justify-content", "flex-end")
    val center = CssValueConst("justify-content", "center")
    val spaceBetween = CssValueConst("justify-content", "space-between")
    val spaceAround = CssValueConst("justify-content", "space-around")
    val spaceEvenly = CssValueConst("justify-content", "space-evenly")
}

object JustifySelf {
    val auto = CssValueConst("justify-self", "auto")
    val normal = CssValueConst("justify-self", "normal")
    val center = CssValueConst("justify-self", "center")
    val start = CssValueConst("justify-self", "start")
    val end = CssValueConst("justify-self", "end")
    val selfStart = CssValueConst("justify-self", "self-start")
    val selfEnd = CssValueConst("justify-self", "self-end")
    val flexStart = CssValueConst("justify-self", "flex-start")
    val flexEnd = CssValueConst("justify-self", "flex-end")
    val left = CssValueConst("justify-self", "left")
    val right = CssValueConst("justify-self", "right")
    val baseline = CssValueConst("justify-self", "baseline")
    val firstBaseline = CssValueConst("justify-self", "first baseline")
    val lastBaseline = CssValueConst("justify-self", "last baseline")
    val stretch = CssValueConst("justify-self", "stretch")
    val safeCenter = CssValueConst("justify-self", "safe center")
    val unsafeCenter = CssValueConst("justify-self", "unsafe center")
}


object Cursor {
    val default = CssValueConst("cursor", "default")
    val pointer = CssValueConst("cursor", "pointer")
    val colResize = CssValueConst("cursor", "col-resize")
    val rowResize = CssValueConst("cursor", "row-resize")
}

object Position {
    val absolute = CssValueConst("position", "absolute")
    val fixed = CssValueConst("position", "fixed")
    val relative = CssValueConst("position", "relative")
    val static = CssValueConst("position", "static")
    val sticky = CssValueConst("position", "sticky")
}

object FontWeight {
    val normal = CssValueConst("font-weight", "normal")
}

object WhiteSpace {
    val normal = CssValueConst("white-space", "normal")
    val nowrap = CssValueConst("white-space", "nowrap")
    val pre = CssValueConst("white-space", "nowrap")
    val preWrap = CssValueConst("white-space", "pre-wrap")
    val preLine = CssValueConst("white-space", "pre-line")
    val breakSpaces = CssValueConst("white-space", "break-spaces")
}

object Overflow {
    val visible = CssValueConst("overflow", "visible")
    val hidden = CssValueConst("overflow", "hidden")
    val clip = CssValueConst("overflow", "clip")
    val scroll = CssValueConst("overflow", "scroll")
    val auto = CssValueConst("overflow", "auto")
    val hiddenVisible = CssValueConst("overflow", "hidden visible")
}

object OverflowX {
    val visible = CssValueConst("overflow-x", "visible")
    val hidden = CssValueConst("overflow-x", "hidden")
    val clip = CssValueConst("overflow-x", "clip")
    val scroll = CssValueConst("overflow-x", "scroll")
    val auto = CssValueConst("overflow-x", "auto")
}

object OverflowY {
    val visible = CssValueConst("overflow-y", "visible")
    val hidden = CssValueConst("overflow-y", "hidden")
    val clip = CssValueConst("overflow-y", "clip")
    val scroll = CssValueConst("overflow-y", "scroll")
    val auto = CssValueConst("overflow-y", "auto")
}

object TextAlign {
    val left = CssValueConst("text-align", "left")
    val right = CssValueConst("text-align", "right")
    val center = CssValueConst("text-align", "center")
    val justify = CssValueConst("text-align", "justify")
    val justifyAll = CssValueConst("text-align", "justify-all")
    val start = CssValueConst("text-align", "start")
    val end = CssValueConst("text-align", "end")
    val matchParent = CssValueConst("text-align", "match-parent")
}

object UserSelect {
    val none = CssValueConst("user-select", "none")
    val auto = CssValueConst("user-select", "auto")
    val text = CssValueConst("user-select", "text")
    val contain = CssValueConst("user-select", "contain")
    val all = CssValueConst("user-select", "all")
}

class CssValueConst(
    val name: String,
    val value: String
)