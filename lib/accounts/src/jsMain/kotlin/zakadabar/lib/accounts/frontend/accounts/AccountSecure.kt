/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.accounts

import zakadabar.lib.accounts.data.AccountListSecureEntry
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.core.browser.crud.ZkQueryCrudTarget

/**
 * CRUD for security officers to manage accounts.
 */
class AccountSecure : ZkQueryCrudTarget<AccountPrivateBo, AccountListSecureEntry>() {

    init {
        companion = AccountPrivateBo.Companion
        boClass = AccountPrivateBo::class
        queryTableClass = AccountsTableSecure::class
        editorClass = Form::class
    }

}