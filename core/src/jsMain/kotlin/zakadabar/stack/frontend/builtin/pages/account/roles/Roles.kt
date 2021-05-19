/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.roles

import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget

object Roles : ZkCrudTarget<RoleDto>() {

    init {
        companion = RoleDto.Companion
        dtoClass = RoleDto::class
        tableClass = Table::class
        pageClass = Form::class
    }

}

