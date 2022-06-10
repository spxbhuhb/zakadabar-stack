/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2 license.
 */

package zakadabar.softui.browser.theme.base

@Suppress("ClassName", "unused")
object Borders {

    const val borderColor = Colors.grey.g300

    object borderWidth {
        val b0 = pxToRem(0)
        val b1 = pxToRem(1)
        val b2 = pxToRem(2)
        val b3 = pxToRem(3)
        val b4 = pxToRem(4)
        val b5 = pxToRem(5)
    }

    object borderRadius {
        val xs = pxToRem(2)
        val sm = pxToRem(4)
        val md = pxToRem(8)
        val lg = pxToRem(12)
        val xl = pxToRem(16)
        val xxl = pxToRem(24)
        val section = pxToRem(160)
    }
}