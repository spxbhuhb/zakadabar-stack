/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.account

import zakadabar.demo.data.account.AccountPrivateDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<AccountPrivateDto>() {

    init {
        title = Strings.accounts
        onCreate = { Accounts.openCreate() }

        + AccountPrivateDto::id
        + AccountPrivateDto::accountName
        + AccountPrivateDto::fullName
        + AccountPrivateDto::id.actions(Accounts)
    }

}