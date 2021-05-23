/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.accounts

import zakadabar.stack.data.builtin.account.AccountPrivateBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<AccountPrivateBo>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.accounts
        crud = Accounts

        + AccountPrivateBo::id
        + AccountPrivateBo::accountName
        + AccountPrivateBo::fullName
        + AccountPrivateBo::email
        + AccountPrivateBo::phone
        + actions()
    }

}