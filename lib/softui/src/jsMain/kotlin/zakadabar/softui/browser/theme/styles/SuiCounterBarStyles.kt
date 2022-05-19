/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.counterbar.CounterBarStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha
import zakadabar.softui.browser.theme.base.Borders

open class SuiCounterBarStyles: ZkCssStyleSheet(), CounterBarStyleSpec {

    var counterBarHeight by cssParameter { 42 }
    override var borderTop by cssParameter { theme.fixBorder }
    override var backgroundColor by cssParameter { theme.textColor.alpha(0.1) }

    override val tableCounterBar by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        + JustifyContent.spaceBetween

        minHeight = counterBarHeight.px
        width = 100.percent

        paddingLeft = 10.px

        fontWeight = 400.weight
        fontSize = 12.px

        borderTop = this@SuiCounterBarStyles.borderTop
        backgroundColor = theme.blockBackgroundColor
        borderBottomLeftRadius = Borders.borderRadius.md
        borderBottomRightRadius = Borders.borderRadius.md
    }
}