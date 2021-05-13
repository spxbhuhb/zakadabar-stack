/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

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
        boxSizing = "border-box"
        width = 18
        height = 18
    }

    val icon20 by cssClass {
        boxSizing = "border-box"
        width = 20
        height = 20
    }

    val icon22 by cssClass {
        boxSizing = "border-box"
        width = 22
        height = 22
    }

    val icon24 by cssClass {
        boxSizing = "border-box"
        width = 24
        height = 24
    }

}