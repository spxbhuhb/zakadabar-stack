/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.counterbar

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.zkTableStyles

open class CounterBar(
    open var text: String,
    open val contextElements: List<ZkElement> = emptyList()
) : ZkElement() {

    override fun onCreate() {
        + zkCounterBarStyles.tableCounterBar

        this.clear()

        + div { + text }
        + row {
            contextElements.forEach {
                + it marginRight 10
            }
        }
    }

}