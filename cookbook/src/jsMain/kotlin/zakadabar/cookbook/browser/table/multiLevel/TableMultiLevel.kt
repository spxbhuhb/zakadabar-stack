/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.multiLevel

import zakadabar.cookbook.browser.table.demoDataLong
import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.table.ZkTableRow
import zakadabar.core.browser.table.columns.ZkLevelColumn
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr

class TableMultiLevel : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        addLocalTitle = true
        search = true
        multiLevel = true

        + ZkLevelColumn(this)
        + ExampleBo::stringValue size 1.fr
        + ExampleBo::booleanValue size 3.em

        + actions {
            + action("hello") { index, row ->
                toastSuccess { "You clicked on 'hello' of ${row.stringValue} (index: $index)." }
            }
            + action("world") { index, row ->
                toastSuccess { "You clicked on 'world' of ${row.stringValue} (index: $index)." }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        demoDataLong(cookbookStyles.inlineTable)
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

    override fun getRowLevel(row: ZkTableRow<ExampleBo>): Int {
        return when (row.index % 10) {
            0 -> 0
            1 -> 0
            2 -> 1
            3 -> 0
            4 -> 1
            5 -> 1
            6 -> 0
            7 -> 1
            8 -> 1
            9 -> 1
            else -> 0
        }
    }

}