/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.table.TableStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.core.util.PublicApi
import zakadabar.core.util.alpha
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

open class SuiTableStyles : SuiFieldStyles(), TableStyleSpec {

    override var tableBackgroundColor by cssParameter { theme.blockBackgroundColor }
    override var headerBackground by cssParameter { theme.blockBackgroundColor }
    override var headerText by cssParameter { theme.textColor }
    override var oddRowBackground by cssParameter { theme.blockBackgroundColor }
    override var textColor by cssParameter { theme.textColor }
    override var hoverBackgroundColor by cssParameter { theme.hoverBackgroundColor }
    override var hoverTextColor by cssParameter { theme.hoverTextColor }
    override var rowBorderColor by cssParameter { theme.borderColor }
    override var headerBottomBorder by cssParameter { theme.fixBorder }
    override var border by cssParameter<String?> { null }
    override var actionTextColor by cssParameter { theme.primaryColor }
    override var controlColor by cssParameter { theme.primaryColor }
    override var rowHeight by cssParameter { 42 }
    override var multiLevelColor by cssParameter { theme.disabledColor.alpha(0.2) }
    override var multiLevelBorder by cssParameter { theme.fixBorder }

    override var fieldHeight by cssParameter { 32 }

    override fun onConfigure() {
        super.onConfigure()
        selectedOption.height = (fieldHeight - 2).px// -2 to compensate border from container
    }

    override val outerContainer by cssClass {
        + Display.flex
        + FlexDirection.column
        width = 100.percent
        boxShadow = BoxShadows.md
        borderRadius = Borders.borderRadius.md
        backgroundColor = tableBackgroundColor
    }

    override val contentContainer by cssClass {
        + Position.relative

        flexGrow = 1.0

        + Overflow.auto

//        paddingLeft = (theme.spacingStep / 2).px
//        paddingRight = (theme.spacingStep / 2).px

        // https://stackoverflow.com/a/16681137/3796844
        styles["-webkit-backface-visibility"] = "hidden"
        styles["-moz-backface-visibility"] = "hidden"
        styles["-webkit-transform"] = "translate3d(0, 0, 0)"
        styles["-moz-transform"] = "translate3d(0, 0, 0)"
    }

    override val withTitle by cssClass {
        borderBottomLeftRadius = Borders.borderRadius.md
        borderBottomRightRadius = Borders.borderRadius.md
    }

    override val withoutTitle by cssClass {
        borderRadius = Borders.borderRadius.md
    }

    override val resizeHandle by cssClass {
        + BoxSizing.borderBox
        + Position.absolute
        top = 0.px
        right = 0.px
        bottom = 0.px
        borderRight = "1px solid $controlColor"
        borderLeft = "1px solid $controlColor"
        backgroundColor = headerBackground
        marginTop = 4.px
        marginBottom = 4.px
        opacity = 0.opacity
        width = 5.px
        cursor = "col-resize"

        on(":hover") {
            opacity = 1.opacity
        }
    }

    override val beingResized by cssClass {
        on(" .$resizeHandle") {
            opacity = 1.opacity + " !important"
        }
    }

    override val otherBeingResized by cssClass {
        on(" .$resizeHandle") {
            opacity = 0.opacity + " !important"
        }
    }

    override val noSelect by cssClass {
        + UserSelect.none

        styles["-moz-user-select"] = "none"
        styles["-webkit-user-select"] = "none"
        styles["-ms-user-select"] = "none"
    }

    override val sortSign by cssClass {
        + BoxSizing.borderBox
        + Position.absolute
        top = 0.px
        right = 10.px
        bottom = 0.px
    }

    override val sortedDescending by cssClass {
        marginTop = 16.px
        marginRight = 12.px
        width = 0.px
        height = 0.px
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderTop = "6px solid $controlColor"
    }

    override val sortedAscending by cssClass {
        marginTop = 16.px
        marginRight = 12.px
        width = 0.px
        height = 0.px
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderBottom = "6px solid $controlColor"
    }

    override val table by cssClass {
        + BoxSizing.borderBox
        + Display.grid

        borderCollapse = "collapse"
        minWidth = 100.percent

        backgroundColor = tableBackgroundColor
    }

    @PublicApi
    override val head by cssClass({ ".$table thead" }) {
        + Display.contents
    }

    @PublicApi
    override val body by cssClass({ ".$table tbody" }) {
        + Display.contents
    }

    @PublicApi
    override val row by cssClass({ ".$table tr" }) {
        + Display.contents
        + Cursor.pointer
    }

    @PublicApi
    override val hoverOverRow by cssClass({ ".$table tr:hover td" }) {
        backgroundColor = hoverBackgroundColor
        color = hoverTextColor
    }

    @PublicApi
    override val headerCell by cssClass({ ".$table th" }) {
        + Position.sticky
        + Overflow.hidden
        + WhiteSpace.nowrap
        + TextAlign.left
        + Display.flex
        + AlignItems.center
        + Cursor.pointer
        + BoxSizing.borderBox

        height = rowHeight.px
        top = 0.px

        zIndex = 30.zIndex

        paddingLeft = 10.px
        textOverflow = "ellipsis"
        textTransform = "uppercase"
        fontSize = 75.percent
        fontWeight = 400.weight
        background = headerBackground

        color = headerText
        borderBottom = headerBottomBorder
    }

    @PublicApi
    override val resizeHandleOn by cssClass({ ".$table th:hover .$resizeHandle" }) {
        opacity = 1.opacity
    }

    @PublicApi
    override val cell by cssClass {
        + Display.flex
        + AlignItems.center
        + BoxSizing.borderBox

        zIndex = 20.zIndex
        paddingLeft = 10.px

        color = textColor
        fill = textColor
        borderBottom = "1px solid $rowBorderColor"
        backgroundColor = oddRowBackground
    }

    override val fixHeight by cssClass {
        height = rowHeight.px
        textOverflow = "ellipsis"
        + Overflow.hidden
        + WhiteSpace.nowrap
    }

    override val variableHeight by cssClass {
        minHeight = rowHeight.px
    }

    @PublicApi
    open val firstCellOfHeader by cssClass({ ".$table th:first-child" }) {
        if (this@SuiTableStyles.border != null) {
            borderLeft = this@SuiTableStyles.border
        }
        paddingLeft = 20.px
    }

//    @PublicApi
//    open val firstCellOfHeaderWithoutTitle by cssClass({ ".$withoutTitle .$table th:first-child" }) {
//        borderTopLeftRadius = Borders.borderRadius.md
//    }
//
//    @PublicApi
//    open val lastCellOfHeaderWithoutTitle by cssClass({ ".$withoutTitle .$table th:last-child" }) {
//        borderTopRightRadius = Borders.borderRadius.md
//    }

    @PublicApi
    override val firstCellOfRow by cssClass({ ".$table tr td:first-child" }) {
        if (this@SuiTableStyles.border != null) {
            borderLeft = this@SuiTableStyles.border
        }
        paddingLeft = 20.px
    }

    @PublicApi
    override val lastCellOfRow by cssClass({ ".$table tr td:last-child" }) {
        if (this@SuiTableStyles.border != null) {
            borderRight = this@SuiTableStyles.border
        }
    }

    override val cellOfLastRowOfTable by cssClass({ ".$table tr:nth-last-child(2) td" }) {
        // borderBottom = "transparent"
    }

    override val leftBottomCellOfTable by cssClass {

    }

    override val rightBottomCellOfTable by cssClass {

    }

    override val dense by cssClass {
        paddingTop = "0 !important"
        paddingBottom = "0 !important"
    }

    override val actions by cssClass {
        + Display.flex
        + AlignItems.center
    }

    override val actionEntry by cssClass {
        + Display.flex
        + AlignItems.center
        height = 100.percent

        + WhiteSpace.nowrap

        paddingLeft = 0.px
        marginRight = theme.spacingStep.px

        fontSize = 75.percent
        lineHeight = 1.3.em
        fontWeight = 400.weight
        color = actionTextColor

        + TextTransform.uppercase
        + Cursor.pointer
    }

    override val multiLevelContainer by cssClass {
        + Display.flex
        + JustifyContent.center
        + AlignItems.center
    }

    override val multiLevelOpen by cssClass {
        paddingLeft = 0.px + "!important" // so the arrow won't be indented too much
        + Display.flex
        + JustifyContent.center
        + AlignItems.center
    }

    override val multiLevelClosed by cssClass {
        paddingLeft = 0.px + "!important" // so the arrow won't be indented too much
        + Display.flex
        + JustifyContent.center
        + AlignItems.center
    }

    override val multiLevelSingle by cssClass {
        backgroundColor = multiLevelColor
        borderRight = multiLevelBorder
    }
}