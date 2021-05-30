/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.theme

import zakadabar.stack.frontend.builtin.layout.tabcontainer.zkTabContainerStyles
import zakadabar.stack.frontend.builtin.layout.zkScrollBarStyles
import zakadabar.stack.frontend.builtin.sidebar.zkSideBarStyles
import zakadabar.stack.frontend.builtin.table.zkTableStyles
import zakadabar.stack.frontend.builtin.titlebar.zkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.util.after
import zakadabar.stack.util.alpha

open class ZkGreenBlueTheme : ZkBuiltinLightTheme() {

    companion object {
        const val NAME = "zakadabar.stack.theme.light.green.blue"
    }

    override val name = NAME

    private val darkGreen = "#538d34"
    private val lightGreen = "#78b641"
    private val darkBlue = "#1d3457"

    override var fontFamily = "Roboto, system-ui, -apple-system, BlinkMacSystemFont, Roboto"

    override var primaryColor = darkGreen
    override var secondaryColor = darkBlue

    override var disabledInputTextColor by after { textColor }
    override var disabledInputBackgroundColor = darkGreen.alpha(0.2)

    override var boxShadow = "none" // "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
    override var fixBorder = "1px solid ${lightGreen.alpha(0.3)}"
    override var blockBackgroundColor = lightGreen.alpha(0.1)

    override fun onResume() {
        with(zkScrollBarStyles) {
            thumbColor = textColor.alpha(0.5)
            trackColor = backgroundColor
        }

        with(zkSideBarStyles) {
            backgroundColor = darkGreen
            hoverTextColor = ZkColors.white
            textColor = ZkColors.white
            fontSize = "90%"
            sectionBackgroundColor = ZkColors.white.alpha(0.1)
            sectionBorderColor = ZkColors.white.alpha(0.5)
            sectionTextColor = ZkColors.white
        }

        with(zkTableStyles) {
            headerBottomBorder = "1px solid darkGreen"
            actionTextColor = primaryColor
            hoverTextColor = theme.textColor
        }

        with(zkTitleBarStyles) {
            appHandleBackground = blockBackgroundColor
            appHandleText = textColor
            appHandleBorder = "1px solid $darkGreen"
            appTitleBarBackground = blockBackgroundColor
            appTitleBarBorder = appHandleBorder
        }

        with(zkTabContainerStyles) {
            activeLabelBackgroundColor = darkGreen
        }
    }

}