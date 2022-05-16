/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme

import zakadabar.core.browser.layout.zkScrollBarStyles
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.browser.theme.ZkBuiltinDarkTheme
import zakadabar.core.browser.theme.softui.components.SuiTheme
import zakadabar.core.resource.ZkColors
import zakadabar.core.util.alpha
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.Colors
import zakadabar.softui.browser.theme.base.rgba

open class SoftUiDarkTheme : ZkBuiltinDarkTheme(), SuiTheme {

    companion object {
        const val NAME = "app-dark"
    }

    override val name = NAME

    override var fontFamily = "'Open Sans'"
    override var fontSize = "16px"
    override var fontWeight = "400"

    override var textColor = Colors.grey.g200

    override var backgroundColor = Colors.black.main

    override var primaryColor = Colors.primary.main
    override var primaryPair = Colors.white.main
    override var secondaryColor = Colors.secondary.main
    override var secondaryPair = ZkColors.white
    override var successColor = Colors.success.main
    override var successPair = Colors.white.main
    override var warningColor = Colors.warning.main
    override var warningPair = Colors.dark.main
    override var dangerColor = Colors.error.main
    override var dangerPair = ZkColors.white
    override var infoColor = Colors.info.main
    override var infoPair = ZkColors.white
    override var disabledColor = Colors.light.main
    override var disabledPair = Colors.dark.main

    override var blockBackgroundColor = Colors.grey.g800
    override val headerTagColor = Colors.light.main
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

        with(zkSideBarStyles) {
            iconSize = 18
        }

        with(zkScrollBarStyles) {
            thumbColor = textColor.alpha(0.5)
            trackColor = backgroundColor
        }

    }

}