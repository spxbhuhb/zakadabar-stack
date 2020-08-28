/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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