/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.counterbar

import zakadabar.core.browser.field.ZkFieldStyles
import zakadabar.core.browser.table.ZkTableStyles
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.resource.css.*

var zkCounterBarStyles by cssStyleSheet(ZkCounterBarStyles())

class ZkCounterBarStyles: ZkFieldStyles() {

    open val tableCounterBar by cssClass {
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

        background = zkTitleBarStyles.localTitleBarBackground
    }
}