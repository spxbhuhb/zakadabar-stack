/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.titlebar

import zakadabar.core.browser.titlebar.ZkAppTitleBar
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.browser.util.plusAssign

class SuiAppTitleBar : ZkAppTitleBar() {

    override fun onCreate() {
        classList += zkTitleBarStyles.appTitleBar

        + titleContainer css zkTitleBarStyles.titleContainer marginRight 10

        + contextElements css zkTitleBarStyles.contextElementContainer

    }

}