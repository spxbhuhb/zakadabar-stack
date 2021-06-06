/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.sub.ContentCategoryBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


/**
 * CRUD target for [ContentCategoryBo].
 * 
 * Generated with Bender at 2021-06-05T02:31:08.496Z.
 */
class ContentCategoryCrud : ZkCrudTarget<ContentCategoryBo>() {
    init {
        companion = ContentCategoryBo.Companion
        boClass = ContentCategoryBo::class
        editorClass = ContentCategoryForm::class
        tableClass = ContentCategoryTable::class
    }
}

/**
 * Form for [ContentCategoryBo].
 * 
 * Generated with Bender at 2021-06-05T02:31:08.496Z.
 */
class ContentCategoryForm : ZkForm<ContentCategoryBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<ContentCategoryForm>()) {
            + section {
                + bo::id
                + bo::name
            }
        }
    }
}

/**
 * Table for [ContentCategoryBo].
 * 
 * Generated with Bender at 2021-06-05T02:31:08.496Z.
 */
class ContentCategoryTable : ZkTable<ContentCategoryBo>() {

    override fun onConfigure() {

        crud = target<ContentCategoryCrud>()

        titleText = translate<ContentCategoryTable>()

        add = true
        search = true
        export = true
        
        + ContentCategoryBo::id
        + ContentCategoryBo::name
        
        + actions()
    }
}