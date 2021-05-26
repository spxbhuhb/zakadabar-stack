/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.accounts

import zakadabar.stack.data.builtin.account.AccountPrivateBo
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget

object Accounts : ZkCrudTarget<AccountPrivateBo>() {
    init {
        companion = AccountPrivateBo.Companion
        boClass = AccountPrivateBo::class
        tableClass = Table::class
        pageClass = Form::class
    }
}