/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.table.ZkTableStyles
import zakadabar.core.browser.theme.softui.components.suiTheme
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

class TableStyles : ZkTableStyles() {

    override var tableBackgroundColor by cssParameter { suiTheme.blockBackgroundColor }
    override var headerBackground by cssParameter { suiTheme.blockBackgroundColor }
    override var oddRowBackground by cssParameter { suiTheme.blockBackgroundColor }

    override val outerContainer by cssClass {
        + Display.flex
        + FlexDirection.column
        width = 100.percent
    }

    override val contentContainer by cssClass {
        + Position.relative

        flexGrow = 1.0

        + Overflow.auto

        backgroundColor = tableBackgroundColor
        boxShadow = BoxShadows.md
        borderRadius = Borders.borderRadius.md

        paddingLeft = (theme.spacingStep / 2).px
        paddingRight = (theme.spacingStep / 2).px
    }

    override val cellOfLastRowOfTable by cssClass({ ".$table tr:nth-last-child(2) td" }) {
        borderBottom = "transparent"
    }

    override val leftBottomCellOfTable by cssClass({ ".$table tr:last-child(2) td:first-child" }) {
        if (this@TableStyles.border != null) {
            borderBottomLeftRadius = Borders.borderRadius.md
        }
    }

    override val rightBottomCellOfTable by cssClass({ ".$table tr:last-child(2) td:last-child" }) {
        if (this@TableStyles.border != null) {
            borderBottomRightRadius = Borders.borderRadius.md
        }
    }
}