/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.RoleBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId


/**
 * Exposed based Persistence API for RoleBo.
 *
 * Generated with Bender at 2021-05-25T19:24:42.583Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class RoleExposedPaGen : ExposedPaBase<RoleBo, RoleExposedTableGen>(
    table = RoleExposedTableGen
) {
    override fun ResultRow.toBo() : RoleBo {
        return RoleBo(
            id = this[table.id].entityId(),
            name = this[RoleExposedTableGen.name],
            description = this[RoleExposedTableGen.description]
        )
    }

    override fun UpdateBuilder<*>.fromBo(bo: RoleBo) {
        this[RoleExposedTableGen.name] = bo.name
        this[RoleExposedTableGen.description] = bo.description
    }
}

/**
 * Exposed based SQL table for RoleBo.
 *
 * Generated with Bender at 2021-05-25T19:24:42.584Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object RoleExposedTableGen : ExposedPaTable<RoleBo>(
    tableName = "role"
) {

    internal val name = varchar("name", 50)
    internal val description = text("description")

}