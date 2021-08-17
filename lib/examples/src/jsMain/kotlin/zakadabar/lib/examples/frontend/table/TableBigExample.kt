/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.table

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.resource.css.OverflowY
import zakadabar.core.resource.css.Position
import zakadabar.core.resource.css.px

/**
 * This example shows a table with 10.000 rows.
 */
class TableBigExample(
    element: HTMLElement,
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + Position.relative
        + OverflowY.hidden
        height = 400.px

        + zkLayoutStyles.fixBorder

        val data = (1..10000).map { TableExampleDto("Rose $it", "rosa $it", it, it % 2 == 1) }

        + ExampleTable().apply {
            search = true
            addLocalTitle = true
            setData(data)
        }
    }

}