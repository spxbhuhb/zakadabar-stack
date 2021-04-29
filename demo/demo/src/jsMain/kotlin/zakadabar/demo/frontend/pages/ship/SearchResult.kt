/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ship.SearchShipsResult
import zakadabar.demo.data.ship.ShipDto
import zakadabar.stack.data.record.StringRecordId
import zakadabar.stack.frontend.builtin.table.ZkTable

class SearchResult : ZkTable<SearchShipsResult>() {

    // This table shows results of a ShipSearchQuery. This one shows just some parts
    // of ship records but you could build any table with composite data. This is why
    // we do not provide the crud for this table, but add onDblClick manually.

    init {
        + SearchShipsResult::shipId
        + SearchShipsResult::name
        + SearchShipsResult::port
        + SearchShipsResult::captain
    }

    override fun getRowId(row: SearchShipsResult): String {
        return row.shipId.toString()
    }

    override fun onDblClick(id: String) {
        Ships.openRead(StringRecordId<ShipDto>(id))
    }

}