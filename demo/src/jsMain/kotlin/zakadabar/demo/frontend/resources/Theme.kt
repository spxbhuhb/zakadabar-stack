/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.resources

import zakadabar.stack.frontend.resources.ZkTheme

object Theme : ZkTheme() {

    val darkGreen = "#538d34"
    val lightGreen = "#78b641"
    val darkBlue = "#1d3457"
    override var gray = "#929292"
    val orange = "#f3910e"

    val title = darkBlue

    object Menu {
        val background = darkGreen
        val text = white
    }

    object PrimaryAction {
        val background = darkGreen
        val foreground = white
    }

    object Action {
        val background = darkBlue
        val foreground = white
    }


}