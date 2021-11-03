/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.tableInTab

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.tabcontainer.tabContainer

class TableInTab : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + tabContainer {
            + tab("1") {
                + TableInTabTable()
            }
            + tab("2") {
                + TableInTabTable()
            }
        }
    }

}