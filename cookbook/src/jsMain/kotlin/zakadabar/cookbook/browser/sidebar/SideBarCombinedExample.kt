/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.sidebar

import zakadabar.cookbook.browser.sidebar.arrowAfter.SideBarArrowAfter
import zakadabar.cookbook.browser.sidebar.arrowOpen.SideBarArrowOpen
import zakadabar.cookbook.browser.sidebar.icons.SideBarWithIcons
import zakadabar.cookbook.cookbookStyles
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.tabcontainer.tabContainer

class SideBarCombinedExample : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + tabContainer {
            + tab("Icons") {
                + cookbookStyles.block
                + SideBarWithIcons()
            }
            + tab("Arrow After") {
                + cookbookStyles.block
                + SideBarArrowAfter()
            }
            + tab("Arrow Open") {
                + cookbookStyles.block
                + SideBarArrowOpen()
            }
        }
    }
}