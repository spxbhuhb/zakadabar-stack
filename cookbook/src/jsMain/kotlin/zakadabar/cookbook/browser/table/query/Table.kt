/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.query

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.data.toEntityId
import zakadabar.core.browser.application.target
import zakadabar.core.browser.page.ZkEntityPage
import zakadabar.core.browser.table.ZkTable

class Table : ZkTable<QueryResultEntry>() {

    override fun onConfigure() {
        super.onConfigure()

        query = Query()

        + QueryResultEntry::field1
        + QueryResultEntry::field2

        actions()
    }

    override fun getRowId(row: QueryResultEntry) =
        row.entityId.toString()

    override fun onDblClick(id: String) =
        target<Details>().open(id.toEntityId())
}

class Details : ZkEntityPage<ExampleBo>() {

    override fun onCreate() {
       + "These are the details for $entityId"
    }

}
