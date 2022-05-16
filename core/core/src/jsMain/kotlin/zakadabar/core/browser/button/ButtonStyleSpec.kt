/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.button

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface ButtonStyleSpec : CssStyleSpec {
    val iconSize: Int
    val buttonHeight: Int

    val text: ZkCssStyleRule
    val combined: ZkCssStyleRule
    val icon: ZkCssStyleRule
    val square: ZkCssStyleRule
    val round: ZkCssStyleRule
    val noShadow: ZkCssStyleRule
    val primaryFill: ZkCssStyleRule
    val primaryNoFill: ZkCssStyleRule
    val primaryBorder: ZkCssStyleRule
    val primaryNoBorder: ZkCssStyleRule
    val secondaryFill: ZkCssStyleRule
    val secondaryNoFill: ZkCssStyleRule
    val secondaryBorder: ZkCssStyleRule
    val secondaryNoBorder: ZkCssStyleRule
    val successFill: ZkCssStyleRule
    val successNoFill: ZkCssStyleRule
    val successBorder: ZkCssStyleRule
    val successNoBorder: ZkCssStyleRule
    val warningFill: ZkCssStyleRule
    val warningNoFill: ZkCssStyleRule
    val warningBorder: ZkCssStyleRule
    val warningNoBorder: ZkCssStyleRule
    val dangerFill: ZkCssStyleRule
    val dangerNoFill: ZkCssStyleRule
    val dangerBorder: ZkCssStyleRule
    val dangerNoBorder: ZkCssStyleRule
    val infoFill: ZkCssStyleRule
    val infoNoFill: ZkCssStyleRule
    val infoBorder: ZkCssStyleRule
    val infoNoBorder: ZkCssStyleRule
    val disabledFill: ZkCssStyleRule
    val disabledNoFill: ZkCssStyleRule
    val disabledBorder: ZkCssStyleRule
    val disabledNoBorder: ZkCssStyleRule
}