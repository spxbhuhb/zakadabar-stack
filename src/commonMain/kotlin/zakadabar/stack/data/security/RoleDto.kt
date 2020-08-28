/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.security

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.DtoWithEntityCompanion
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.extend.DtoWithEntityContract

/**
 * A role which has some business specific meaning. May be granted to
 * principals by adding a [RoleGrantDto] child to the given principal entity.
 */
@Serializable
class RoleDto(

    override val id: Long,
    override val entityDto: EntityDto?

) : DtoWithEntityContract<RoleDto> {

    override fun getType() = type

    override fun comm() = comm

    companion object : DtoWithEntityCompanion<RoleDto>() {
        override val type = "${Stack.shid}/role"
    }
}