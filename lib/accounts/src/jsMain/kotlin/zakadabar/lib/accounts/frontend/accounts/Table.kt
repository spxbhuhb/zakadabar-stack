/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.accounts

import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<AccountPrivateBo>() {

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = stringStore.accounts
        crud = target<Accounts>()

        + AccountPrivateBo::id
        + AccountPrivateBo::accountName
        + AccountPrivateBo::fullName
        + AccountPrivateBo::email
        + AccountPrivateBo::phone
        + actions()
    }

}