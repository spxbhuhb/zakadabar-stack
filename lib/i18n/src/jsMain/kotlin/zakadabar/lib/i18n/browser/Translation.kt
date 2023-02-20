/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.browser

import zakadabar.core.browser.application.target
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.data.TranslationBo


/**
 * CRUD target for [TranslationBo].
 *
 * Generated with Bender at 2021-05-30T09:38:55.859Z.
 */
class TranslationCrud : ZkCrudTarget<TranslationBo>() {
    init {
        companion = TranslationBo.Companion
        boClass = TranslationBo::class
        editorClass = TranslationForm::class
        tableClass = TranslationTable::class
    }
}

/**
 * Form for [TranslationBo].
 *
 * Generated with Bender at 2021-05-30T09:38:55.859Z.
 */
class TranslationForm : ZkForm<TranslationBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<TranslationForm>()) {
            + section {
                + bo::id
                + bo::key
                + bo::locale query { LocaleBo.all().by { it.name } }
                + bo::value
            }
        }
    }
}

/**
 * Table for [TranslationBo].
 *
 * Generated with Bender at 2021-05-30T09:38:55.860Z.
 */
class TranslationTable : ZkTable<TranslationBo>() {

    val locales by preload { LocaleBo.allAsMap() }
    override fun onConfigure() {

        crud = target<TranslationCrud>()

        titleText = localized<TranslationTable>()

        + TranslationBo::id // record id and opt record id is not supported yet
        + TranslationBo::key
        + optString { locales[locale]?.name } label localizedStrings.locale
        + TranslationBo::value

        + actions()
    }
}