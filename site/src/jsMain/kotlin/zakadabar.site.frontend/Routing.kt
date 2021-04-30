/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.site.frontend

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.demo.lib.frontend.crud.BuiltinCrud
import zakadabar.demo.lib.frontend.form.FormFieldsGenerated
import zakadabar.demo.lib.frontend.form.SyntheticForm
import zakadabar.demo.lib.frontend.input.CheckboxList
import zakadabar.demo.lib.frontend.layout.TabContainer
import zakadabar.demo.lib.frontend.modal.Modals
import zakadabar.demo.lib.frontend.pages.ArgPage
import zakadabar.demo.lib.frontend.query.QueryPage
import zakadabar.demo.lib.frontend.table.FetchedTable
import zakadabar.demo.lib.frontend.table.GeneratedTable
import zakadabar.demo.lib.frontend.toast.Toasts
import zakadabar.site.frontend.pages.misc.*
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.builtin.pages.account.accounts.Accounts
import zakadabar.stack.frontend.builtin.pages.account.login.Login
import zakadabar.stack.frontend.builtin.pages.resources.locales.Locales
import zakadabar.stack.frontend.builtin.pages.resources.settings.Settings
import zakadabar.stack.frontend.builtin.pages.resources.translations.Translations

object Routing : ZkAppRouting(DefaultLayout, Landing) {

    init {
        + Landing
        + Content
        + CodeLab
        + GettingStarted
        + Highlights

        + ArgPage
        + BuiltinCrud
        + CheckboxList
        + Modals
        + FormFieldsGenerated
        + TabContainer
        + GeneratedTable
        + FetchedTable
        + QueryPage
        + Toasts
        + SyntheticForm

        + Accounts
        + Login
        + Roles
        + Settings
        + Locales
        + Translations
    }

}