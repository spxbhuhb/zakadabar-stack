package zakadabar.lib.accounts.backend

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.PrincipalBo
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPaBase
import zakadabar.stack.backend.data.exposed.ExposedPaTable
import zakadabar.stack.util.BCrypt

/**
 * Exposed based Persistence API for PrincipalBo.
 *
 * Generated with Bender at 2021-05-25T19:06:56.247Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class PrincipalExposedPaGen : ExposedPaBase<PrincipalBo,PrincipalExposedTableGen>(
    table = PrincipalExposedTableGen
) {
    override fun ResultRow.toBo() : PrincipalBo {
        return PrincipalBo(
            id = this[table.id].entityId(),
            validated = this[table.validated],
            locked = this[table.locked],
            expired = this[table.expired],
            credentials = null /* do not send out the secret */,
            resetKey = null /* do not send out the secret */,
            resetKeyExpiration = this[table.resetKeyExpiration]?.toKotlinInstant(),
            lastLoginSuccess = this[table.lastLoginSuccess]?.toKotlinInstant(),
            loginSuccessCount = this[table.loginSuccessCount],
            lastLoginFail = this[table.lastLoginFail]?.toKotlinInstant(),
            loginFailCount = this[table.loginFailCount]
        )
    }

    override fun UpdateBuilder<*>.fromBo(bo: PrincipalBo) {
        this[table.validated] = bo.validated
        this[table.locked] = bo.locked
        this[table.expired] = bo.expired
        this[table.credentials] = bo.credentials?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[table.resetKey] = bo.resetKey?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[table.resetKeyExpiration] = bo.resetKeyExpiration?.toJavaInstant()
        this[table.lastLoginSuccess] = bo.lastLoginSuccess?.toJavaInstant()
        this[table.loginSuccessCount] = bo.loginSuccessCount
        this[table.lastLoginFail] = bo.lastLoginFail?.toJavaInstant()
        this[table.loginFailCount] = bo.loginFailCount
    }
}

/**
 * Exposed based SQL table for PrincipalBo.
 *
 * Generated with Bender at 2021-05-25T19:06:56.248Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object PrincipalExposedTableGen : ExposedPaTable<PrincipalBo>(
    tableName = "principal"
) {

    internal val validated = bool("validated")
    internal val locked = bool("locked")
    internal val expired = bool("expired")
    internal val credentials = varchar("credentials", 200).nullable()
    internal val resetKey = varchar("reset_key", 200).nullable()
    internal val resetKeyExpiration = timestamp("reset_key_expiration").nullable()
    internal val lastLoginSuccess = timestamp("last_login_success").nullable()
    internal val loginSuccessCount = integer("login_success_count")
    internal val lastLoginFail = timestamp("last_login_fail").nullable()
    internal val loginFailCount = integer("login_fail_count")

}