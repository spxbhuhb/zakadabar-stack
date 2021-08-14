/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.accounts

import zakadabar.lib.accounts.data.AccountListSecure
import zakadabar.lib.accounts.data.AccountListSecureEntry
import zakadabar.core.data.entity.EntityId
import zakadabar.core.frontend.application.target
import zakadabar.core.frontend.builtin.table.ZkTable
import zakadabar.core.frontend.resources.css.em
import zakadabar.core.resource.localizedStrings

class AccountsTableSecure : ZkTable<AccountListSecureEntry>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true

        titleText = localizedStrings.accounts

        + AccountListSecureEntry::accountId
        + AccountListSecureEntry::accountName
        + AccountListSecureEntry::fullName
        + AccountListSecureEntry::email
        + AccountListSecureEntry::phone
        + AccountListSecureEntry::locale size 5.em
        + AccountListSecureEntry::validated
        + AccountListSecureEntry::locked
        + AccountListSecureEntry::expired
        + AccountListSecureEntry::anonymized

        + actions() size 4.em
    }

    override fun onResume() {
        super.onResume()
        setData(AccountListSecure())
    }

    override fun getRowId(row: AccountListSecureEntry): String {
        return row.accountId.toString()
    }

    override fun onAddRow() {
        target<AccountSecure>().openCreate()
    }

    override fun onDblClick(id: String) {
        target<AccountSecure>().openUpdate(EntityId(id))
    }

}