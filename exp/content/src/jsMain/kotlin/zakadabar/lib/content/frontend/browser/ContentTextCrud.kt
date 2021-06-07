/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.ContentCommonBo
import zakadabar.lib.content.data.ContentTextBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


/**
 * CRUD target for [ContentTextBo].
 * 
 * Generated with Bender at 2021-06-07T02:56:36.422Z.
 */
class ContentTextCrud : ZkCrudTarget<ContentTextBo>() {
    init {
        companion = ContentTextBo.Companion
        boClass = ContentTextBo::class
        editorClass = ContentTextForm::class
        tableClass = ContentTextTable::class
    }
}

/**
 * Form for [ContentTextBo].
 * 
 * Generated with Bender at 2021-06-07T02:56:36.422Z.
 */
class ContentTextForm : ZkForm<ContentTextBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<ContentTextForm>()) {
            + section {
                + bo::id
                + select(bo::content) { ContentCommonBo.all().by { it.title } }
                + bo::type
                + bo::value
            }
        }
    }
}

/**
 * Table for [ContentTextBo].
 * 
 * Generated with Bender at 2021-06-07T02:56:36.422Z.
 */
class ContentTextTable : ZkTable<ContentTextBo>() {

    override fun onConfigure() {

        crud = target<ContentTextCrud>()

        titleText = translate<ContentTextTable>()

        add = true
        search = true
        export = true
        
        + ContentTextBo::id
        // ContentTextBo::content // record id and opt record id is not supported yet 
        + ContentTextBo::type
        + ContentTextBo::value
        
        + actions()
    }
}