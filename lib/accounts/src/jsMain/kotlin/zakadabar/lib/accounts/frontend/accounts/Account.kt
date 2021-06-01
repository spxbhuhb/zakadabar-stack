/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.accounts

import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.executor
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.io

class Account : ZkPage() {

    override fun onCreate() {
        + zkLayoutStyles.p1

        io {
            + Form().also {
                it.bo = AccountPrivateBo.read(EntityId(executor.account.id))
                it.mode = ZkElementMode.Update
            }
        }
    }

}