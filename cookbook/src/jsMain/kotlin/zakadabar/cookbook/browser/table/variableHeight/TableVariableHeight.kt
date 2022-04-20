/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.variableHeight

import zakadabar.cookbook.browser.table.demoDataLong
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr

class TableVariableHeight : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        fixRowHeight = false

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
        demoDataLong()
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

}