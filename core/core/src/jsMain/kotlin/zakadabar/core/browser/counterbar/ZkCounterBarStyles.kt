/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.counterbar

import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

open class ZkCounterBarStyles: ZkCssStyleSheet(), CounterBarStyleSpec {

    override var borderTop by cssParameter { theme.fixBorder }
    override var backgroundColor by cssParameter { theme.textColor.alpha(0.1) }

    override val tableCounterBar by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center
        + JustifyContent.spaceBetween

        minHeight = 26.px
        maxHeight = 26.px
        width = 100.percent

        paddingLeft = 10.px

        fontWeight = 400.weight
        fontSize = 12.px

        borderTop = this@ZkCounterBarStyles.borderTop
        backgroundColor = this@ZkCounterBarStyles.backgroundColor
    }
}