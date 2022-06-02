/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.sidebar.arrow

import zakadabar.cookbook.cookbookStyles
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.tabcontainer.tabContainer

class SideBarCombinedExample : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + tabContainer {
            + tab("Arrow After") {
                + cookbookStyles.block
                + SideBarArrowAfter()
            }
            + tab("Arrow Open") {
                + cookbookStyles.block
                + SideBarArrowOpen()
            }
            + tab("Arrow Close") {
                + cookbookStyles.block
                + SideBarArrowClose()
            }
            + tab("Arrow Open/Close") {
                + cookbookStyles.block
                + SideBarArrowBoth()
            }
            + tab("Arrow Size") {
                + cookbookStyles.block
                + SideBarArrowSize()
            }
        }
    }
}