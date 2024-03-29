/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.layout

import zakadabar.core.resource.css.Display
import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px

var zkScrollBarStyles by cssStyleSheet(ZkScrollBarStyles())

@Suppress("unused") // these are global styles interpreted by the browser
open class ZkScrollBarStyles : ZkCssStyleSheet() {

    open var enabled: Boolean = true
    open var scrollBarWidth: Int = 12
    open var scrollBarHeight: Int = 12
    open var thumbColor: String = "yellow"
    open var trackColor: String = "red"

    open val webkitScrollBar by cssRule("::-webkit-scrollbar") {
        if (enabled) {
            width = scrollBarWidth.px
            height = scrollBarHeight.px
        }
    }

    open val webkitScrollBarThumb by cssRule("::-webkit-scrollbar-thumb") {
        if (enabled) {
            backgroundColor = thumbColor
        }
    }

    open val webkitScrollBarTrack by cssRule("::-webkit-scrollbar-track") {
        if (enabled) {
            backgroundColor = trackColor
        }
    }

    open val hideScrollBar by cssClass {
        styles["scrollbar-width"] = "none"
        on("::-webkit-scrollbar") {
            + Display.none
        }
    }

}