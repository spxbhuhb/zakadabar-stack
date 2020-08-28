/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoWithRecordCompanion
import zakadabar.stack.extend.DtoWithRecordContract

@Serializable
data class EntityDto(
    override val id: Long,
    val acl: Long?,
    val status: EntityStatus,
    val parentId: Long?,
    val type: String,
    val name: String,
    val size: Long,
    val revision: Long,
    val modifiedBy: Long,
    val modifiedAt: Long
) : DtoWithRecordContract<EntityDto> {

    companion object : DtoWithRecordCompanion<EntityDto>() {

        fun new(parentId: Long?, type: String, name: String) = EntityDto(
            id = 0,
            acl = null,
            status = EntityStatus.Active,
            parentId = parentId,
            type = type,
            name = name,
            size = 0,
            revision = 1,
            modifiedAt = 0,
            modifiedBy = 0
        )

    }

    fun requireId(id: Long): EntityDto {
        require(this.id == id)
        return this
    }

    fun requireType(type: String): EntityDto {
        require(this.type == type)
        return this
    }

    override fun comm() = comm
}


