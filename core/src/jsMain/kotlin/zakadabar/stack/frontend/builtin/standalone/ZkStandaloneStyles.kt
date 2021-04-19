/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.standalone

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkStandaloneStyles : ZkCssStyleSheet<ZkTheme>() {

    val standaloneInput by cssClass {

        display = "block"
        color = "#444"
        padding = ".3em 1.4em .3em .8em"
        boxSizing = "border-box"
        margin = 0
        border = "1px solid #aaa"
        mozAppearance = "none"
        webkitAppearance = "none"
        appearance = "none"
        backgroundColor = "#fff"
        borderRadius = 2
        minHeight = 26

        on(":hover") {
            borderColor = "#888"
        }

        on(":focus") {
            outline = "none"
        }

        on(" option") {
            fontWeight = "normal"
        }

        on(":disabled") {
            color = "gray"
            backgroundColor = "#gray"
            borderColor = "#aaa"
        }

        on(":disabled:hover") {
            borderColor = "#aaa"
        }

        on("[aria-disabled=true]") {
            color = "gray"
            borderColor = "#aaa"
        }
    }

    init {
        attach()
    }
}