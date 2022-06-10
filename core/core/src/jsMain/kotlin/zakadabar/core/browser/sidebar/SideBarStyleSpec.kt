/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import zakadabar.core.resource.ZkIconSource
import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface SideBarStyleSpec : CssStyleSpec {

    var groupOpenIcon : ZkIconSource
    var groupCloseIcon : ZkIconSource
    var afterGroupOpenIcon : ZkIconSource
    var afterGroupCloseIcon : ZkIconSource

    var backgroundColor: String
    var textColor: String
    var itemMinHeight: Int
    var fontSize: String
    var iconSize: Int
    var hoverTextColor: String
    var sectionBackgroundColor: String
    var sectionTextColor: String
    var sectionBorderColor: String

    val sidebar: ZkCssStyleRule
    val item: ZkCssStyleRule
    val itemText: ZkCssStyleRule
    val icon: ZkCssStyleRule
    val groupTitle: ZkCssStyleRule
    val groupArrow: ZkCssStyleRule
    val groupContent: ZkCssStyleRule
    val sectionTitle: ZkCssStyleRule
    val sectionCloseIcon: ZkCssStyleRule
    val sectionContent: ZkCssStyleRule

}