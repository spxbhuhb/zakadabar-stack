/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.accounts

import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget

class Accounts : ZkCrudTarget<AccountPrivateBo>() {
    init {
        companion = AccountPrivateBo.Companion
        boClass = AccountPrivateBo::class
        tableClass = Table::class
        pageClass = Form::class
    }
}