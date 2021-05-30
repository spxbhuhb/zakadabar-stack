package zakadabar.lib.i18n.frontend

import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.data.TranslationBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


/**
 * CRUD target for [TranslationBo].
 *
 * Generated with Bender at 2021-05-30T09:38:55.859Z.
 */
class Translations : ZkCrudTarget<TranslationBo>() {
    init {
        companion = TranslationBo.Companion
        boClass = TranslationBo::class
        pageClass = TranslationForm::class
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

        build(translate<TranslationForm>()) {
            + section {
                + bo::id
                + bo::key
                + select(bo::locale) { LocaleBo.all().by { it.name } }
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

    override fun onConfigure() {

        crud = target<Translations>()

        titleText = translate<TranslationTable>()

        add = true
        search = true
        export = true

        // TranslationBo::id // record id and opt record id is not supported yet
        + TranslationBo::key
        // TranslationBo::locale // record id and opt record id is not supported yet
        + TranslationBo::value

        + actions()
    }
}