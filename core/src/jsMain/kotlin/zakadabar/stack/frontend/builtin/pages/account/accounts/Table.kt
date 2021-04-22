/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.accounts

import zakadabar.stack.data.builtin.account.AccountPrivateDto
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<AccountPrivateDto>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = strings.accounts
        crud = Accounts

        + AccountPrivateDto::id
        + AccountPrivateDto::accountName
        + AccountPrivateDto::fullName
        + AccountPrivateDto::email
        + AccountPrivateDto::phone
        + actions()
    }

}