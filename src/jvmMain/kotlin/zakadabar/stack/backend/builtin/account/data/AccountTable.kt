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

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.Stack
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.data.security.CommonAccountDto

object AccountTable : IdTable<Long>("t_${Stack.shid}_accounts") {
    override val id = reference("id", EntityTable)

    val emailAddress = varchar("email_address", 100)
    val fullName = varchar("full_name", 100)
    val displayName = varchar("display_name", 20)
    val organizationName = varchar("organization_name", 100)
    val avatar = reference("avatar", EntityTable).nullable()

    override val primaryKey = PrimaryKey(id)

    fun toDto(row: ResultRow) = CommonAccountDto(
        id = row[id].value,
        entityDto = null, // do not include entity by default
        emailAddress = row[emailAddress],
        fullName = row[fullName],
        displayName = row[displayName],
        organizationName = row[organizationName],
        avatar = row[avatar]?.value
    )

}