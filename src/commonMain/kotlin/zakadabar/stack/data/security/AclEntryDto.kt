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

package zakadabar.stack.data.security

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.DtoWithEntityCompanion
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.extend.DtoWithEntityContract

/**
 * An access control list entry. Used for fine grained security checks.
 *
 * @property  subjectId  The id of the entity this entry grants/denies access for.
 * @property  access     A free-form access type name. There are some common names but
 *                       anything is acceptable as long as the backend recognizes it.
 * @property  allow      When true, the access type is allowed, when false it is
 *                       explicitly denied. False stops the access check process and
 *                       denies that type of access immediately.
 */
@Serializable
data class AclEntryDto(

    override val id: Long,
    override val entityDto: EntityDto?,

    val subjectId: Long,
    val access: String,
    val allow: Boolean

) : DtoWithEntityContract<AclEntryDto> {

    override fun getType() = type

    override fun comm() = comm

    companion object : DtoWithEntityCompanion<AclEntryDto>() {
        override val type = "${Stack.shid}/acl-entry"
    }
}