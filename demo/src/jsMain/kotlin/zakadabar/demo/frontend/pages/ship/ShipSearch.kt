/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ShipsByName
import zakadabar.stack.frontend.builtin.simple.SimpleInput
import zakadabar.stack.frontend.elements.ZkPage
import zakadabar.stack.frontend.util.launch

object ShipSearch : ZkPage() {

    private val input = SimpleInput(enter = true) { searchText -> onSearch(searchText, table) }
    private val table = Table()

    override fun init() = build {
        + column {
            + input
            + table
        }
    }

    private fun onSearch(searchText: String, table: Table) = launch {
        table.setData(ShipsByName(name = searchText).execute())
    }

}