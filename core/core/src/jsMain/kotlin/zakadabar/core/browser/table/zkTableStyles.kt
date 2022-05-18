/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import zakadabar.core.browser.field.ZkFieldStyles
import zakadabar.core.resource.css.*
import zakadabar.core.util.PublicApi
import zakadabar.core.util.alpha

var zkTableStyles : TableStyleSpec by cssStyleSheet(ZkTableStyles())

open class ZkTableStyles : ZkFieldStyles(), TableStyleSpec {

    override var tableBackgroundColor by cssParameter { theme.backgroundColor }
    override var headerBackground by cssParameter { theme.backgroundColor }
    override var headerText by cssParameter { theme.textColor }
    override var oddRowBackground by cssParameter { theme.backgroundColor }
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
        height = 100.percent
    }

    override val contentContainer by cssClass {
        + Position.relative

        flexGrow = 1.0

        + Overflow.auto

        backgroundColor = theme.backgroundColor
        boxShadow = theme.boxShadow
        borderRadius = 2.px
    }

    override val withTitle by cssClass {

    }

    override val withoutTitle by cssClass {

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
    override val firstCellOfRow by cssClass({ ".$table tr td:first-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderLeft = this@ZkTableStyles.border
        }
    }

    @PublicApi
    override val lastCellOfRow by cssClass({ ".$table tr td:last-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderRight = this@ZkTableStyles.border
        }
    }

    @PublicApi
    override val cellOfLastRowOfTable by cssClass({ ".$table tr:last-child td" }) {
        if (this@ZkTableStyles.border != null) {
            borderBottom = this@ZkTableStyles.border
        }
    }

    @PublicApi
    override val leftBottomCellOfTable by cssClass({ ".$table tr:last-child td:first-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderBottomLeftRadius = 4.px
        }
    }

    override val rightBottomCellOfTable by cssClass({ ".$table tr:last-child td:last-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderBottomRightRadius = 4.px
        }
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

    }

    override val multiLevelOpen by cssClass {

    }

    override val multiLevelClosed by cssClass {
    }

    override val multiLevelSingle by cssClass {
        backgroundColor = multiLevelColor
        borderRight = multiLevelBorder
    }
}