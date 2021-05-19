/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.accounts

import zakadabar.stack.data.builtin.account.AccountPrivateDto
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget

object Accounts : ZkCrudTarget<AccountPrivateDto>() {
    init {
        companion = AccountPrivateDto.Companion
        dtoClass = AccountPrivateDto::class
        tableClass = Table::class
        pageClass = Form::class
    }
}