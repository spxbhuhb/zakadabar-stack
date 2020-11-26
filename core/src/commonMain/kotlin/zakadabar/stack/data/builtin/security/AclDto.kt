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
 * An access control list. It is basically a folder for [AclEntryDto] entities.
 * Referenced by the [EntityRecordDto.acl].
 */
@Serializable
class AclDto(

    override val id: Long,
    override val entityRecord: EntityRecordDto?

) : EntityDto<AclDto> {

    override fun getRecordType() = recordType

    override fun comm() = comm

    companion object : EntityDtoCompanion<AclDto>() {
        override val recordType = "${Stack.shid}/acl"
    }
}