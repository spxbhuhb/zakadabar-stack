/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.site.frontend

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.demo.frontend.lib.crud.BuiltinCrud
import zakadabar.demo.frontend.lib.form.FormFields
import zakadabar.demo.frontend.lib.input.CheckboxList
import zakadabar.demo.frontend.lib.layout.TabContainer
import zakadabar.demo.frontend.lib.modal.ConfirmDialog
import zakadabar.demo.frontend.lib.pages.ArgPage
import zakadabar.demo.frontend.lib.query.QueryPage
import zakadabar.demo.frontend.lib.table.FetchedTable
import zakadabar.demo.frontend.lib.table.GeneratedTable
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
        + ConfirmDialog
        + FormFields
        + TabContainer
        + GeneratedTable
        + FetchedTable
        + QueryPage

        + Accounts
        + Login
        + Roles
        + Settings
        + Locales
        + Translations
    }

}