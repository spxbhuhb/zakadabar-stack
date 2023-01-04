/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.export.xlsx

import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.lib.xlsx.browser.onExportXlsx

class XlsxTableExport :  ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        addLocalTitle = true
        export = true
        exportHeaders = true

        + ExampleBo::stringValue size 10.em
        + ExampleBo::booleanValue size 1.fr
        + ExampleBo::doubleValue
        + ExampleBo::localDateValue
    }

    override fun onCreate() {
        super.onCreate()
        demoData(5000)
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

    override fun onExportCsv() = onExportXlsx()

}