/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.resources

import zakadabar.stack.frontend.builtin.button.ZkButtonTheme
import zakadabar.stack.frontend.builtin.icon.ZkIconTheme
import zakadabar.stack.frontend.builtin.menu.ZkMenuTheme
import zakadabar.stack.frontend.builtin.table.ZkTableTheme

open class ZkTheme {

    val white = "#ffffff"
    val black = "#000000"

    val activeBlue = "#2746ab"
    val inactiveBlue = "#bec7e6";
    val green = "#89e6c2"

    open var darkestColor = "#0d5b28"
    open var darkColor = "#2e8d36"
    open var lightColor = "#43cd50"
    open var lightestColor = "#fff"
    open var darkestGray = "#7b7b7b"
    open var darkGray = "#acabab"
    open var gray = "#d9d9d9"
    open var lightGray = "#f5f5f5"

    open var infoColor = "#6f90e5"
    open var errorColor = "#D71313"
    open var approveColor = darkColor
    open var cancelColor = "#bfbe96"
    open var selectedColor = "#486cc7"

    open var headerBackground = "rgba(13,91,40,0.05)"
    open var headerForeground = "#0d5b28"
    open var headerIconBackground = "0d5b28"
    open var headerIconFill = "$43cd50"
    open var headerToolBackground = "rgba(13,91,40,0.05)"
    open var headerToolFill = "#2e8d36"

    open var sliderColor = lightGray
    open var fontFamily = "'Lato', sans-serif"
    open var fontSize = 12
    open var fontWeight = 300

    open var borderRadius = 2

    open var margin = 8

    open var contentWidth = 600
    open var headerHeight = 26

    val infoBackground = "#81d4fa"
    val infoText = black

    val successBackground = "#087f23"
    val successText = white

    val warningBackground = "#ffea00"
    val warningText = black

    val errorBackground = "#c41c00"
    val errorText = white

    open var icon = ZkIconTheme(
        background = "transparent",
        foreground = white
    )

    open var button = ZkButtonTheme()

    open var menu = ZkMenuTheme(
        background = activeBlue,
        hoverBackground = "rgba(255,255,255,0.2)",
        activeBackground = inactiveBlue,
        text = white
    )

    open var table = ZkTableTheme(
        headerBackground = "#6c7ae0",
        headerText = white,
        oddRowBackground = white,
        evenRowBackground = "#f8f6ff",
        text = "#505050",
        hoverBackground = "#6c7ae0",
        hoverText = black,
        border = "1px solid #6c7ae0"
    )

}