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

package zakadabar.stack.backend.builtin.account.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.data.security.CommonAccountDto

class AccountDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccountDao>(AccountTable)

    var emailAddress by AccountTable.emailAddress
    var fullName by AccountTable.fullName
    var displayName by AccountTable.displayName
    var organizationName by AccountTable.organizationName
    var avatar by AccountTable.avatar

    fun toDto() = CommonAccountDto(
        id = id.value,
        entityDto = null, // do not include entity by default
        emailAddress = emailAddress,
        fullName = fullName,
        displayName = displayName,
        organizationName = organizationName,
        avatar = avatar?.value
    )
}