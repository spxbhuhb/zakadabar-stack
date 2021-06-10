/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.ContentStereotypeBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.resources.localized


/**
 * CRUD target for [ContentStereotypeBo].
 *
 * Generated with Bender at 2021-06-10T04:08:05.307Z.
 */
class ContentStereotypeCrud : ZkCrudTarget<ContentStereotypeBo>() {
    init {
        companion = ContentStereotypeBo.Companion
        boClass = ContentStereotypeBo::class
        editorClass = ContentStereotypeForm::class
        tableClass = ContentStereotypeTable::class
    }
}

/**
 * Form for [ContentStereotypeBo].
 *
 * Generated with Bender at 2021-06-10T04:08:05.308Z.
 */
class ContentStereotypeForm : ZkForm<ContentStereotypeBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<ContentStereotypeForm>()) {
            + section {
                + bo::id
                + select(bo::parent) { ContentStereotypeBo.all().by { it.key.localized } }
                + bo::key
            }
        }
    }
}

/**
 * Table for [ContentStereotypeBo].
 *
 * Generated with Bender at 2021-06-10T04:08:05.308Z.
 */
class ContentStereotypeTable : ZkTable<ContentStereotypeBo>() {

    override fun onConfigure() {

        crud = target<ContentStereotypeCrud>()

        titleText = translate<ContentStereotypeTable>()

        add = true
        search = true
        export = true

        + ContentStereotypeBo::id
        // ContentStereotypeBo::parent // record id and opt record id is not supported yet
        + ContentStereotypeBo::key

        + actions()
    }
}