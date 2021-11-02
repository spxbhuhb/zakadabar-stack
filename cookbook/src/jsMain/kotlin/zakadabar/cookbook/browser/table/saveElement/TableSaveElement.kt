/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.saveElement

import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.application.application
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.resource.css.px

class TableSaveElement : ZkElement() {

    // This is an important part. Saving the table into the companion object
    // lets us use it later. However, this works only if you use the table
    // only on one page. If you use the table on more than one page, you
    // have to declare a variable for each page.

    companion object {
        var savedTable: Table? = null
    }

    override fun onCreate() {
        super.onCreate()

        + row {
            + buttonPrimary("Save State and Go Back") {
                application.back()
            } marginRight 20.px

            + buttonPrimary("Clear the state and Go Back") {
                // this drops the current table state
                // the next display of the table will build from scratch
                savedTable = null
                application.back()
            }
        } marginBottom 20.px

        // Here, we check if the we have a saved table. If we have one
        // we use it, otherwise we just create one. The [openEditor]
        // parameter is not important, it is here to have the table on
        // the same page as the editor.

        if (savedTable == null) {
            + Table(::openEditor).also { savedTable = it }
        } else {

            // Have to call rebuild, so the table will apply the data changes
            // and will scroll to the proper position.
            //
            // The re-set of openEditor is needed in this example because we
            // re-use the table in another element. So, function references
            // would point to the old parent which is wrong.

            + savedTable?.apply {
                openEditor = ::openEditor
                rebuild()
            }
        }
    }

    // This function is not needed if the application navigation state changes.
    // The example stays on the same page and replaces the table with the editor
    // on the fly.
    //
    // Notice the `copy`. It is important not to modify the table data directly.
    // For example if the user changes something in the editor and then clicks
    // "back" without saving the changes, the table should contain the original data.

    fun openEditor(bo: ExampleBo) {
        - savedTable
        + Editor(::backToTable).also { it.bo = bo.copy() }
    }

    // This function is not needed if the application navigation state changes.
    // The example stays on the same page and replaces the table with the editor
    // on the fly.

    fun backToTable() {
        - first<Editor>()
        + savedTable?.apply { rebuild() }
    }
}

class Table(
    var openEditor: (bo: ExampleBo) -> Unit
) : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        addLocalTitle = true
        export = true
        search = true

        + ExampleBo::stringValue size 10.em
        + ExampleBo::booleanValue size 1.fr
        + actions()
    }

    override fun onCreate() {
        super.onCreate()
        demoData()
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) {
        openEditor(getRowData(id))
    }

}

class Editor(
    val backToTable: () -> Unit
) : ZkForm<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()
        mode = ZkElementMode.Other
    }

    override fun onCreate() {
        super.onCreate()
        + bo::stringValue
        + bo::booleanValue
        + buttons()
    }

    override fun validate(submit: Boolean): Boolean {
        // this bo is actually invalid because of many other fields
        // so we'll just skip validation here
        return true
    }

    // This is an important function, because here you have to update the table
    // data. You can do

    override fun onSubmitSuccess() {
        TableSaveElement.savedTable?.apply {
            setRowData(bo)
        }
        backToTable()
    }

    override var onBack = { backToTable() }
}

