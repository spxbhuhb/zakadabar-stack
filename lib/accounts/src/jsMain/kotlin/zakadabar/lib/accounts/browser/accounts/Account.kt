/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.accounts

import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.application.executor
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.util.io
import zakadabar.core.data.EntityId
import zakadabar.lib.accounts.data.AccountPrivateBo

/**
 * Page for the users to view/change their own account information.
 */
class Account : ZkPage() {

    override fun onCreate() {
        io {
            + Form().also {
                it.bo = AccountPrivateBo.read(EntityId(executor.account.accountId))
                it.mode = ZkElementMode.Update
            }
        }
    }

}