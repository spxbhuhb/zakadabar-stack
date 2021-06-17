/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.StatusBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.resources.localized


/**
 * CRUD target for [StatusBo].
 * 
 * Generated with Bender at 2021-06-05T02:07:27.688Z.
 */
class StatusCrud : ZkCrudTarget<StatusBo>() {
    init {
        companion = StatusBo.Companion
        boClass = StatusBo::class
        editorClass = StatusForm::class
        tableClass = StatusTable::class
    }
}

/**
 * Form for [StatusBo].
 * 
 * Generated with Bender at 2021-06-05T02:07:27.689Z.
 */
class StatusForm : ZkForm<StatusBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<StatusForm>()) {
            + section {
                + bo::id
                + bo::name
            }
        }
    }
}

/**
 * Table for [StatusBo].
 * 
 * Generated with Bender at 2021-06-05T02:07:27.689Z.
 */
class StatusTable : ZkTable<StatusBo>() {

    override fun onConfigure() {

        crud = target<StatusCrud>()

        titleText = localized<StatusTable>()

        add = true
        search = true
        export = true
        
        + StatusBo::id
        + StatusBo::name
        
        + actions()
    }
}