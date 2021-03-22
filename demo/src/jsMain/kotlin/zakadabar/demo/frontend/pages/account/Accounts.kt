/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.account

import zakadabar.demo.data.AccountPrivateDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Accounts : ZkCrudTarget<AccountPrivateDto>() {
    init {
        companion = AccountPrivateDto.Companion
        dtoClass = AccountPrivateDto::class
        tableClass = Table::class
        pageClass = Form::class
    }
}