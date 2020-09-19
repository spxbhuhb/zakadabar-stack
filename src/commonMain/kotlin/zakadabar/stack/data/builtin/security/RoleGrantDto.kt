/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.security

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.data.entity.EntityDtoCompanion
import zakadabar.stack.data.entity.EntityRecordDto

/**
 * When child of an entity, that entity has the given role.
 * May be used for security checks.
 */
@Serializable
data class RoleGrantDto(

    override val id: Long,
    override val entityRecord: EntityRecordDto?,

    val roleId: Long

) : EntityDto<RoleGrantDto> {

    override fun getType() = type

    override fun comm() = comm

    companion object : EntityDtoCompanion<RoleGrantDto>() {
        override val type = "${Stack.shid}/role-grant"
    }
}