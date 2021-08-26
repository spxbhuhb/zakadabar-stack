/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.action

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.util.default


class TableCustomActions : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        + ExampleBo::stringValue size 10.em
        + ExampleBo::booleanValue size 1.fr

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

        + cookbookStyles.smallInlineTable

        val template = default<ExampleBo> { }

        val data = (1..50).map {
            template.copy(id = EntityId(it.toLong()), stringValue = "row $it", booleanValue = (it % 2 == 0))
        }

        setData(data)
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

}