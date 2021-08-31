/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.slider

import zakadabar.core.resource.css.*

var zkSliderStyles by cssStyleSheet(ZkSliderStyles())

open class ZkSliderStyles : ZkCssStyleSheet() {

    // FIXME these colors are wrong

    open val horizontalSlider by cssClass {
        + BoxSizing.borderBox
        + Cursor.rowResize

        width = 100.percent
        height = 3.px
        minHeight = 3.px
        backgroundColor = theme.textColor

        on(":hover") {
            backgroundColor = theme.hoverBackgroundColor
        }
    }

    open val verticalSlider by cssClass {
        + BoxSizing.borderBox
        + Cursor.colResize

        height = 100.percent
        width = 3.px
        minWidth = 3.px
        backgroundColor = theme.textColor

        on(":hover") {
            backgroundColor = theme.hoverBackgroundColor
        }
    }

}