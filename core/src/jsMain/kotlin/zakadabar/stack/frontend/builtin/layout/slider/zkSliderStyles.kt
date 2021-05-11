/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.slider

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkSliderStyles by cssStyleSheet(ZkSliderStyles())

class ZkSliderStyles : ZkCssStyleSheet<ZkTheme>() {

    // FIXME these colors are wrong

    val horizontalSlider by cssClass {
        boxSizing = "border-box"
        width = "100%"
        height = 3
        minHeight = 3
        backgroundColor = theme.textColor
        cursor = "row-resize"
        on(":hover") {
            backgroundColor = theme.hoverBackgroundColor
        }
    }

    val verticalSlider by cssClass {
        boxSizing = "border-box"
        height = "100%"
        width = 3
        minWidth = 3
        backgroundColor = theme.textColor
        cursor = "col-resize"
        on(":hover") {
            backgroundColor = theme.hoverBackgroundColor
        }
    }

}