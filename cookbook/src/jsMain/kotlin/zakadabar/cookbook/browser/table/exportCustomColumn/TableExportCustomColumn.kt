/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.exportCustomColumn

import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.text.csvEscape


class TableExportCustomColumn : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        addLocalTitle = true
        export = true

        + ExampleBo::stringValue size 10.em
        + custom {
            render = { if (it.booleanValue) + "custom value 1" else + "custom value 2" }
            exportCsv = { if (booleanValue) "export value 1".csvEscape() else "export value 2".csvEscape() }
        } size 1.fr
    }

    override fun onCreate() {
        super.onCreate()
        demoData()
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit
}


