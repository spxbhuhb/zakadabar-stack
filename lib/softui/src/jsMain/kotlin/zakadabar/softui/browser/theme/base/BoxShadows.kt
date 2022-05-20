/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.softui.browser.theme.base

@Suppress("ClassName", "unused")
object BoxShadows {

    val black = Colors.black
    val light = Colors.light
    val white = Colors.white
    val info = Colors.info
    val inputColors = Colors.inputColors

    val xs = boxShadow(0, 2, 9, - 5, Colors.black.main, 0.15)
    val sm = boxShadow(0, 5, 10, 0, Colors.black.main, 0.12)
    val md = "${boxShadow(0, 4, 6, - 1, Colors.black.light, 0.12)}, ${boxShadow(0, 2, 4, - 1, Colors.black.light, 0.07)}"
    val lg = "${boxShadow(0, 8, 26, - 4, Colors.black.light, 0.15)}, ${boxShadow(0, 8, 9, - 5, Colors.black.light, 0.06)}"
    val xl = boxShadow(0, 23, 45, - 11, Colors.black.light, 0.25)
    val xxl = boxShadow(0, 20, 27, 0, Colors.black.main, 0.05)
    val inset = boxShadow(0, 1, 2, 0, Colors.black.main, 0.075, "inset")
    val navbarBoxShadow = "${boxShadow(0, 0, 1, 1, Colors.white.main, 0.9, "inset")}, ${boxShadow(0, 20, 27, 0, Colors.black.main, 0.05)}"

    object buttonBoxShadow {
        val main = "${boxShadow(0, 4, 7, - 1, Colors.black.main, 0.11)}, ${boxShadow(0, 2, 4, - 1, Colors.black.main, 0.07)}"
        val stateOf = "${boxShadow(0, 3, 5, - 1, Colors.black.main, 0.09)}, ${boxShadow(0, 2, 5, - 1, Colors.black.main, 0.07)}"
        val stateOfNotHover = boxShadow(0, 0, 0, 3.2, Colors.info.main, 0.5)
    }

    object inputBoxShadow {
        val focus = boxShadow(0, 0, 0, 2, Colors.inputColors.boxShadow, 1)
        val error = boxShadow(0, 0, 0, 2, Colors.inputColors.error, 0.6)
        val success = boxShadow(0, 0, 0, 2, Colors.inputColors.success, 0.6)
    }

    object sliderBoxShadow {
        val thumb = boxShadow(0, 1, 13, 0, Colors.black.main, 0.2)
    }

    object tabsBoxShadow {
        val indicator = boxShadow(0, 1, 5, 1, Colors.tabs.indicator.boxShadow, 1)
    }
}