/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

lateinit var roleBl : RoleBlProvider

interface RoleBlProvider {
    fun getByName(name : String) : EntityId<out BaseBo>
}