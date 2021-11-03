/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.tableInTab

import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.px
import zakadabar.core.resource.localized

class TableInTabTable : ZkTable<ExampleBo>() {

    override fun onConfigure() {

        height = 400.px
        + zkLayoutStyles.fixBorder

        // Options for the table.

        add = false // do not show the add action in the title
        search = true // show the search action in the title
        export = true // show the export action in the title

        // This is the title of the table. As we use the local title bar
        // we want to show some title.

        titleText = localized<TableInTabTable>()
        addLocalTitle = true

       demoData()

        // You can add columns by the properties. If you don't set a label, the
        // table will use the localized property name.

        + ExampleBo::id size 4.em
        + ExampleBo::doubleValue size 4.em
        + ExampleBo::enumSelectValue
        + ExampleBo::localDateValue label "Date".localized

        // Or you can add them with getters, useful for nested data structures.
        // In this case label is necessary as the property name does not exists,
        // so it is not possible to translate it properly.

        + boolean { booleanValue } label "Boolean".localized
        + optBoolean { optBooleanValue } label "OptBoolean".localized
        + optEntityId { optRecordSelectValue } size 4.em label "Reference".localized

        // Actions for the user to execute on the given row. The default shows
        // a "DETAILS" link. Details performs the same action as the
        // "onDblClick" of the table.

        + actions()
    }

    override fun onDblClick(id: String) {
        toastSuccess { "You clicked on details of $id!" }
    }

}
