/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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


