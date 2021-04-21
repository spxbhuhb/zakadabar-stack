/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.account

import zakadabar.stack.data.builtin.account.AccountPrivateDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<AccountPrivateDto>() {

    init {
        titleText = Strings.accounts
        crud = Accounts

        + AccountPrivateDto::id
        + AccountPrivateDto::accountName
        + AccountPrivateDto::fullName
        + actions()
    }

}