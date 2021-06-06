/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.ContentBo
import zakadabar.lib.content.data.sub.ContentCategoryBo
import zakadabar.lib.content.data.sub.ContentStatusBo
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


/**
 * CRUD target for [ContentBo].
 * 
 * Generated with Bender at 2021-06-05T06:12:00.480Z.
 */
class ContentCrud : ZkCrudTarget<ContentBo>() {
    init {
        companion = ContentBo.Companion
        boClass = ContentBo::class
        editorClass = ContentForm::class
        tableClass = ContentTable::class
    }
}

/**
 * Form for [ContentBo].
 * 
 * Generated with Bender at 2021-06-05T06:12:00.481Z.
 */
class ContentForm : ZkForm<ContentBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<ContentForm>()) {
            + section {
                + bo::id
                + bo::modifiedAt
                + select(bo::modifiedBy) { AccountPublicBo.all().by { it.fullName } }
                + select(bo::status) { ContentStatusBo.all().by { it.name } }
                + select(bo::category) { ContentCategoryBo.all().by { it.name } }
                + select(bo::locale) { LocaleBo.all().by { it.name } }
                + bo::title
                + bo::summary
                + bo::motto
            }
        }
    }
}

/**
 * Table for [ContentBo].
 * 
 * Generated with Bender at 2021-06-05T06:12:00.482Z.
 */
class ContentTable : ZkTable<ContentBo>() {

    override fun onConfigure() {

        crud = target<ContentCrud>()

        titleText = translate<ContentTable>()

        add = true
        search = true
        export = true
        
        + ContentBo::id
        + ContentBo::modifiedAt
        // ContentBo::modifiedBy // record id and opt record id is not supported yet 
        // ContentBo::status // record id and opt record id is not supported yet 
        // ContentBo::category // record id and opt record id is not supported yet 
        // ContentBo::parent // record id and opt record id is not supported yet 
        // ContentBo::locale // record id and opt record id is not supported yet 
        + ContentBo::title
        + ContentBo::summary
        + ContentBo::motto
        
        + actions()
    }
}