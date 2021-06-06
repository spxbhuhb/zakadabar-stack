/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.sub.ContentStatusBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


/**
 * CRUD target for [ContentStatusBo].
 * 
 * Generated with Bender at 2021-06-05T02:07:27.688Z.
 */
class ContentStatusCrud : ZkCrudTarget<ContentStatusBo>() {
    init {
        companion = ContentStatusBo.Companion
        boClass = ContentStatusBo::class
        editorClass = ContentStatusForm::class
        tableClass = ContentStatusTable::class
    }
}

/**
 * Form for [ContentStatusBo].
 * 
 * Generated with Bender at 2021-06-05T02:07:27.689Z.
 */
class ContentStatusForm : ZkForm<ContentStatusBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<ContentStatusForm>()) {
            + section {
                + bo::id
                + bo::name
            }
        }
    }
}

/**
 * Table for [ContentStatusBo].
 * 
 * Generated with Bender at 2021-06-05T02:07:27.689Z.
 */
class ContentStatusTable : ZkTable<ContentStatusBo>() {

    override fun onConfigure() {

        crud = target<ContentStatusCrud>()

        titleText = translate<ContentStatusTable>()

        add = true
        search = true
        export = true
        
        + ContentStatusBo::id
        + ContentStatusBo::name
        
        + actions()
    }
}