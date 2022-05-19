/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import zakadabar.core.browser.field.FieldStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface TableStyleSpec : FieldStyleSpec {

    var tableBackgroundColor: String
    var headerBackground: String
    var headerText: String
    var oddRowBackground: String
    var textColor: String
    var hoverBackgroundColor: String
    var hoverTextColor: String
    var rowBorderColor: String
    var headerBottomBorder: String
    var border: String?
    var actionTextColor: String
    var controlColor: String
    var rowHeight: Int
    var multiLevelColor: String
    var multiLevelBorder: String

    override var fieldHeight: Int

    val outerContainer: ZkCssStyleRule
    val contentContainer: ZkCssStyleRule

    /** Added to the content container when localTitle is true. */
    val withTitle: ZkCssStyleRule

    /** Added to the content container when localTitle is false. */
    val withoutTitle: ZkCssStyleRule
    val resizeHandle: ZkCssStyleRule
    val beingResized: ZkCssStyleRule
    val otherBeingResized: ZkCssStyleRule
    val noSelect: ZkCssStyleRule
    val sortSign: ZkCssStyleRule
    val sortSignContainer: ZkCssStyleRule
    val sortedDescending: ZkCssStyleRule
    val sortedAscending: ZkCssStyleRule
    val table: ZkCssStyleRule
    val head: ZkCssStyleRule
    val body: ZkCssStyleRule
    val row: ZkCssStyleRule
    val hoverOverRow: ZkCssStyleRule
    val headerCell: ZkCssStyleRule
    val headerCellFixHeight: ZkCssStyleRule
    val resizeHandleOn: ZkCssStyleRule
    val cell: ZkCssStyleRule
    val fixHeight: ZkCssStyleRule
    val variableHeight: ZkCssStyleRule
    val firstCellOfRow: ZkCssStyleRule
    val lastCellOfRow: ZkCssStyleRule
    val cellOfLastRowOfTable: ZkCssStyleRule
    val leftBottomCellOfTable: ZkCssStyleRule
    val rightBottomCellOfTable: ZkCssStyleRule
    val dense: ZkCssStyleRule
    val actions: ZkCssStyleRule
    val actionEntry: ZkCssStyleRule
    val multiLevelContainer: ZkCssStyleRule
    val multiLevelOpen: ZkCssStyleRule
    val multiLevelClosed: ZkCssStyleRule
    val multiLevelSingle: ZkCssStyleRule
}