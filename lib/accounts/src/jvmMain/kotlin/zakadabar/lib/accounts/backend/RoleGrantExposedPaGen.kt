package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.RoleGrantBo
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPaBase
import zakadabar.stack.backend.data.exposed.ExposedPaTable


/**
 * Exposed based Persistence API for RoleGrantBo.
 *
 * Generated with Bender at 2021-05-25T19:25:57.124Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class RoleGrantExposedPaGen : ExposedPaBase<RoleGrantBo,RoleGrantExposedTableGen>(
    table = RoleGrantExposedTableGen
) {
    override fun ResultRow.toBo() : RoleGrantBo {
        table as RoleGrantExposedTableGen
        return RoleGrantBo(
            id = this[table.id].entityId(),
            principal = this[table.principal].entityId(),
            role = this[table.role].entityId()
        )
    }

    override fun UpdateBuilder<*>.fromBo(bo: RoleGrantBo) {
        table as RoleGrantExposedTableGen
        this[table.principal] = bo.principal.toLong()
        this[table.role] = bo.role.toLong()
    }
}

/**
 * Exposed based SQL table for RoleGrantBo.
 *
 * Generated with Bender at 2021-05-25T19:25:57.125Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object RoleGrantExposedTableGen : ExposedPaTable<RoleGrantBo>(
    tableName = "role_grant"
) {

    internal val principal = reference("principal", PrincipalExposedTableGen)
    internal val role = reference("role", RoleExposedTableGen)

}