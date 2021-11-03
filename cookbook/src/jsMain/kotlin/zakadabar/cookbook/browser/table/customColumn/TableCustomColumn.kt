/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.customColumn

import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.text.csvEscape


class TableCustomColumn : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        addLocalTitle = true
        search = true
        export = true

        + ExampleBo::stringValue size 10.em

        + custom {
            label = "Custom Value"

            // This is what the column shows. This is a builder for a ZkElement, so
            // anything can go here as long as it does not change the height of the
            // column.

            render = { + if (it.booleanValue) "custom value 1" else "custom value 2" }

            // The matcher is used for search. When it returns with true the row
            // matcher the search text. Be careful here, best is to search in
            // the data the user sees. This example actually does not do that,
            // therefore the table acts "strange" from user point of view.

            matcher = { row, text -> text in row.booleanValue.toString() }

            // Sorter is called whenever the user clicks on a header to sort
            // the column. This has to sort fullData. As with the matcher,
            // be careful here, the user expects rows to be sorted by display
            // text, not the data value.

            sorter = {
                table.fullData = if (sortAscending) {
                    table.fullData.sortedBy { it.data.booleanValue }
                } else {
                    table.fullData.sortedByDescending { it.data.booleanValue }
                }
            }

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


