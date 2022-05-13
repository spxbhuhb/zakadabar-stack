/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.softui.browser.theme.base

// FIXME copyright notices

/**
The pxToRem() function helps you to convert a px unit into a rem unit,
 */
fun pxToRem(number: Number, base: Double = 16.0): String =
    "${number.toDouble() / base}rem"

fun String.hexToDec(start: Int, end: Int): String =
    this.substring(start, end).toInt(16).toString()

fun String.toRgb(): String {
    require(this.startsWith('#')) { "color $this does not start with #" }

    return when (this.length) {
        7 -> "${hexToDec(1, 3)}, ${hexToDec(3, 5)}, ${hexToDec(5, 4)}"
        4 -> "${hexToDec(1, 2)}, ${hexToDec(2, 3)}, ${hexToDec(3, 4)}"
        else -> throw IllegalArgumentException("color does not follow pattern #rgb nor #rrggbb")
    }
}

fun rgba(color : String, opacity : Number) : String =
    "rgba(${color.toRgb()}, ${opacity})"

fun boxShadow(x : Number, y : Number, blur : Number, spread : Number, color : String, opacity : Number, inset : String = "") =
    "$inset ${pxToRem(x)} ${pxToRem(y)} ${pxToRem(blur)} ${pxToRem(spread)} ${rgba(color, opacity)}"

fun linearGradient(color : String, colorState : String, angle : Number = 310) : String =
  "linear-gradient(${angle}deg, ${color}, ${colorState})"