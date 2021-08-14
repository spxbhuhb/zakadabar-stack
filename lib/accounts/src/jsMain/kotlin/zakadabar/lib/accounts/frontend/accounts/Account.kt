/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.accounts

import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.core.data.entity.EntityId
import zakadabar.core.frontend.application.executor
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import zakadabar.core.frontend.builtin.pages.ZkPage
import zakadabar.core.frontend.util.io

/**
 * Page for the users to view/change their own account information.
 */
class Account : ZkPage() {

    override fun onCreate() {
        + zkLayoutStyles.p1

        io {
            + Form().also {
                it.bo = AccountPrivateBo.read(EntityId(executor.account.accountId))
                it.mode = ZkElementMode.Update
            }
        }
    }

}