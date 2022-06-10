/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.tabcontainer

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface TabContainerStyleSpec : CssStyleSpec {

    var labelTextColor: String
    var labelBackgroundColor: String
    var labelHeight: Int
    var activeLabelTextColor: String
    var activeLabelBackgroundColor: String
    var labelBottomBorder: String
    var tabBackgroundColor: String
    val container: ZkCssStyleRule
    val labels: ZkCssStyleRule
    val label: ZkCssStyleRule
    val activeLabel: ZkCssStyleRule
    val contentContainer: ZkCssStyleRule
    val scrolledContent: ZkCssStyleRule
    val border : ZkCssStyleRule
    val padding : ZkCssStyleRule

}