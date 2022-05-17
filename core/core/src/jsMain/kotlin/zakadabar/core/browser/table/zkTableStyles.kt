/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import zakadabar.core.browser.field.ZkFieldStyles
import zakadabar.core.resource.css.*
import zakadabar.core.util.PublicApi
import zakadabar.core.util.alpha

var zkTableStyles by cssStyleSheet(ZkTableStyles())

open class ZkTableStyles : ZkFieldStyles() {

    open var tableBackgroundColor by cssParameter { theme.backgroundColor }
    open var headerBackground by cssParameter { theme.backgroundColor }
    open var headerText by cssParameter { theme.textColor }
    open var oddRowBackground by cssParameter { theme.backgroundColor }
    open var textColor by cssParameter { theme.textColor }
    open var hoverBackgroundColor by cssParameter { theme.hoverBackgroundColor }
    open var hoverTextColor by cssParameter { theme.hoverTextColor }
    open var rowBorderColor by cssParameter { theme.borderColor }
    open var headerBottomBorder by cssParameter { theme.fixBorder }
    open var border by cssParameter<String?> { null }
    open var actionTextColor by cssParameter { theme.primaryColor }
    open var controlColor by cssParameter { theme.primaryColor }
    open var rowHeight by cssParameter { 42 }
    open var multiLevelColor by cssParameter { theme.disabledColor.alpha(0.2) }
    open var multiLevelBorder by cssParameter { theme.fixBorder }

    override var fieldHeight by cssParameter { 32 }

    override fun onConfigure() {
        super.onConfigure()
        selectedOption.height = (fieldHeight - 2).px// -2 to compensate border from container
    }

    open val outerContainer by cssClass {
        + Display.flex
        + FlexDirection.column
        width = 100.percent
        height = 100.percent
    }

    open val contentContainer by cssClass {
        + Position.relative

        flexGrow = 1.0

        + Overflow.auto

        backgroundColor = theme.backgroundColor
        boxShadow = theme.boxShadow
        borderRadius = 2.px
    }

    open val resizeHandle by cssClass {
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

    open val beingResized by cssClass {
        on(" .$resizeHandle") {
            opacity = 1.opacity + " !important"
        }
    }

    open val otherBeingResized by cssClass {
        on(" .$resizeHandle") {
            opacity = 0.opacity + " !important"
        }
    }

    open val noSelect by cssClass {
        + UserSelect.none

        styles["-moz-user-select"] = "none"
        styles["-webkit-user-select"] = "none"
        styles["-ms-user-select"] = "none"
    }

    open val sortSign by cssClass {
        + BoxSizing.borderBox
        + Position.sticky
        top = 0.px
        right = 10.px
        bottom = 0.px
    }

    open val sortSignContainer by cssClass {
        + BoxSizing.borderBox
        + Position.absolute
        top = 0.px
        right = 10.px
        bottom = 0.px
        background = theme.backgroundColor
        margin = 8.px
        padding = 8.px
        border = "1px solid ${theme.primaryColor}"
    }

    open val sortedDescending by cssClass {
        width = 0.px
        height = 0.px
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderTop = "6px solid $controlColor"
    }

    open val sortedAscending by cssClass {
        width = 0.px
        height = 0.px
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderBottom = "6px solid $controlColor"
    }

    open val table by cssClass {
        + BoxSizing.borderBox
        + Display.grid

        borderCollapse = "collapse"
        minWidth = 100.percent

        backgroundColor = tableBackgroundColor
    }

    @PublicApi
    open val head by cssClass({ ".$table thead" }) {
        + Display.contents
    }

    @PublicApi
    open val body by cssClass({ ".$table tbody" }) {
        + Display.contents
    }

    @PublicApi
    open val row by cssClass({ ".$table tr" }) {
        + Display.contents
        + Cursor.pointer
    }

    @PublicApi
    open val hoverOverRow by cssClass({ ".$table tr:hover td" }) {
        backgroundColor = hoverBackgroundColor
        color = hoverTextColor
    }

    @PublicApi
    open val headerCell by cssClass({ ".$table th" }) {
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
    open val resizeHandleOn by cssClass({ ".$table th:hover .$resizeHandle" }) {
        opacity = 1.opacity
    }

    @PublicApi
    open val cell by cssClass {
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

    open val fixHeight by cssClass {
        height = rowHeight.px
        textOverflow = "ellipsis"
        + Overflow.hidden
        + WhiteSpace.nowrap
    }

    open val variableHeight by cssClass {
        minHeight = rowHeight.px
    }

    @PublicApi
    open val firstCellOfRow by cssClass({ ".$table tr td:first-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderLeft = this@ZkTableStyles.border
        }
    }

    @PublicApi
    open val lastCellOfRow by cssClass({ ".$table tr td:last-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderRight = this@ZkTableStyles.border
        }
    }

    @PublicApi
    open val cellOfLastRowOfTable by cssClass({ ".$table tr:last-child td" }) {
        if (this@ZkTableStyles.border != null) {
            borderBottom = this@ZkTableStyles.border
        }
    }

    @PublicApi
    open val leftBottomCellOfTable by cssClass({ ".$table tr:last-child td:first-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderBottomLeftRadius = 4.px
        }
    }

    open val rightBottomCellOfTable by cssClass({ ".$table tr:last-child td:last-child" }) {
        if (this@ZkTableStyles.border != null) {
            borderBottomRightRadius = 4.px
        }
    }

    open val dense by cssClass {
        paddingTop = "0 !important"
        paddingBottom = "0 !important"
    }

    open val actions by cssClass {
        + Display.flex
        + AlignItems.center
    }

    open val actionEntry by cssClass {
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

    open val multiLevelOpen by cssClass {
    }

    open val multiLevelClosed by cssClass {
    }

    open val multiLevelSingle by cssClass {
        backgroundColor = multiLevelColor
        borderRight = multiLevelBorder
    }
}