/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.icon

import org.w3c.dom.HTMLElement
import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*

var zkIconStyles by cssStyleSheet(ZkIconStyles())

open class ZkIconStyles : ZkCssStyleSheet() {

    open fun setInlineStyles(htmlElement: HTMLElement, size: Int) {
        with(htmlElement.style) {
            boxShadow = "border-box"
            width = "${size}px"
            height = "${size}px"
        }
    }

    open val icon18 by cssClass {
        + BoxSizing.borderBox
        width = 18.px
        height = 18.px
    }

    open val icon20 by cssClass {
        + BoxSizing.borderBox
        width = 20.px
        height = 20.px
    }

    open val icon22 by cssClass {
        + BoxSizing.borderBox
        width = 22.px
        height = 22.px
    }

    open val icon24 by cssClass {
        + BoxSizing.borderBox
        width = 24.px
        height = 24.px
    }

    open val notificationCounterText by cssClass {
        + JustifyContent.center
        + JustifySelf.center
        + Display.grid
        fontSize = 65.percent
        fontWeight = 100.percent
        color = ZkColors.white
    }

    open val notificationIconStyle by cssClass {
        position = "absolute"
        top = "0"
//        left = 11.px
    }

    open val notificationIconContainer by cssClass {
        position = "relative"
    }

    open val notificationCountDotContainer by cssClass {
        alignItems = "center"
        borderRadius = 24.px
        backgroundColor = zakadabar.core.resource.theme.dangerColor
        fontWeight = 20.percent
        height = 10.px
    }

}