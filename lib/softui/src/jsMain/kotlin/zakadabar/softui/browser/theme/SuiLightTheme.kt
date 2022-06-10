/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme

import zakadabar.core.browser.layout.zkScrollBarStyles
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.browser.theme.ZkBuiltinLightTheme
import zakadabar.core.browser.theme.softui.components.SuiTheme
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.resource.ZkColors
import zakadabar.core.util.after
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.Colors
import zakadabar.softui.browser.theme.base.rgba

open class SuiLightTheme : ZkBuiltinLightTheme(), SuiTheme {

    companion object {
        const val NAME = "app-light"
    }

    override val name = NAME

    override var fontFamily = "'Open Sans'"
    override var fontSize = "14px"
    override var fontWeight = "400"

    override var textColor = Colors.grey.g800

    override var backgroundColor = Colors.grey.g100

    override var primaryColor = Colors.primary.main
    override var primaryPair = Colors.white.main
    override var secondaryColor = Colors.secondary.main
    override var secondaryPair = ZkColors.white
    override var successColor = Colors.success.main
    override var successPair = Colors.white.main
    override var warningColor = Colors.warning.main
    override var warningPair by after { textColor }
    override var dangerColor = Colors.error.main
    override var dangerPair = ZkColors.white
    override var infoColor = Colors.info.main
    override var infoPair = ZkColors.white
    override var disabledColor = Colors.light.main
    override var disabledPair = Colors.dark.main

    override var blockBackgroundColor = Colors.white.main
    override val headerTagColor = Colors.dark.main
    override val backgroundImage = "linear-gradient(310deg, rgba(20, 23, 39, 0.8), rgba(58, 65, 111, 0.8)), url(\"/curved14.12c9ea54425c4f1bc1d7.jpg\")"
    override val colorOnImage = Colors.light.main
    override val sectionBorder = "${Borders.borderWidth.b0} solid ${rgba(Colors.black.main, 0.125)}"

    // -------------------------------------------------------------------------
    // Customize theme variables
    // -------------------------------------------------------------------------

//    override var primaryColor = "green"

    // -------------------------------------------------------------------------
    // Customize style variables
    // -------------------------------------------------------------------------

    override fun onResume() {

        with(zkTitleBarStyles) {
            appHandleText = Colors.light.main
            appTitleBarText = Colors.light.main
        }

        with(zkSideBarStyles) {
            iconSize = 18
            textColor = Colors.grey.g600
        }

        with(zkScrollBarStyles) {
            enabled = false
//            thumbColor = textColor.alpha(0.5)
//            trackColor = backgroundColor
        }

    }

}