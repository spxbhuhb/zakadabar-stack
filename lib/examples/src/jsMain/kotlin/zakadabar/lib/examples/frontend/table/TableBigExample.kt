/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.table

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles

/**
 * This example shows a table with 10.000 rows.
 */
class TableBigExample(
    element: HTMLElement,
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        style {
            position = "relative"
            height = "400px"
            overflowY = "hidden"
        }

        + zkLayoutStyles.fixBorder

        val data = (1..10000).map { TableExampleDto("Rose $it", "rosa $it", it, it % 2 == 1) }

        + ExampleTable().apply {
            search = true
            addLocalTitle = true
            setData(data)
        }
    }

}