/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.i18n.frontend

import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.core.browser.application.target
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized


/**
 * CRUD target for [LocaleBo].
 *
 * Generated with Bender at 2021-06-07T02:42:38.715Z.
 */
class LocaleCrud : ZkCrudTarget<LocaleBo>() {
    init {
        companion = LocaleBo.Companion
        boClass = LocaleBo::class
        editorClass = LocaleForm::class
        tableClass = LocaleTable::class
    }
}

/**
 * Form for [LocaleBo].
 *
 * Generated with Bender at 2021-06-07T02:42:38.715Z.
 */
class LocaleForm : ZkForm<LocaleBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<LocaleForm>()) {
            + section {
                + bo::id
                + bo::name
                + bo::description
                + bo::status
            }
        }
    }
}

/**
 * Table for [LocaleBo].
 *
 * Generated with Bender at 2021-06-07T02:42:38.715Z.
 */
class LocaleTable : ZkTable<LocaleBo>() {

    override fun onConfigure() {

        crud = target<LocaleCrud>()

        titleText = localized<LocaleTable>()

        add = true
        search = true
        export = true

        + LocaleBo::id
        + LocaleBo::name
        + LocaleBo::description
        + LocaleBo::status

        + actions()
    }
}