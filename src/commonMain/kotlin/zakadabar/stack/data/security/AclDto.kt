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
 * An access control list. It is basically a folder for [AclEntryDto] entities.
 * Referenced by the [EntityDto.acl].
 */
@Serializable
class AclDto(

    override val id: Long,
    override val entityDto: EntityDto?

) : DtoWithEntityContract<AclDto> {

    override fun getType() = type

    override fun comm() = comm

    companion object : DtoWithEntityCompanion<AclDto>() {
        override val type = "${Stack.shid}/acl"
    }
}