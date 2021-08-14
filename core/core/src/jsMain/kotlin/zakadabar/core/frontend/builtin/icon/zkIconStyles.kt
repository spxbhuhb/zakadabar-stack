/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.icon

import org.w3c.dom.HTMLElement
import zakadabar.core.frontend.resources.css.BoxSizing
import zakadabar.core.frontend.resources.css.ZkCssStyleSheet
import zakadabar.core.frontend.resources.css.cssStyleSheet
import zakadabar.core.frontend.resources.css.px

val zkIconStyles by cssStyleSheet(ZkIconStyles())

open class ZkIconStyles : ZkCssStyleSheet() {

    open fun setInlineStyles(htmlElement: HTMLElement, size: Int) {
        with(htmlElement.style) {
            boxShadow = "border-box"
            width = "${size}px"
            height = "${size}px"
        }
    }

    val icon18 by cssClass {
        + BoxSizing.borderBox
        width = 18.px
        height = 18.px
    }

    val icon20 by cssClass {
        + BoxSizing.borderBox
        width = 20.px
        height = 20.px
    }

    val icon22 by cssClass {
        + BoxSizing.borderBox
        width = 22.px
        height = 22.px
    }

    val icon24 by cssClass {
        + BoxSizing.borderBox
        width = 24.px
        height = 24.px
    }

}