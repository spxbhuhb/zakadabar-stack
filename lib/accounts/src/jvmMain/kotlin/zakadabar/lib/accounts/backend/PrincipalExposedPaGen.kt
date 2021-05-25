/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.accounts.data.PrincipalBo
import zakadabar.stack.backend.data.entity.EntityPersistenceApi
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPersistenceApi
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.BCrypt

/*
 * Exposed based Persistence API for PrincipalBo.
 * 
 * Generated with Bender at 2021-05-24T05:50:19.246Z.
 *
 * IMPORTANT: Please do not modify this file, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class PrincipalExposedPaGen : EntityPersistenceApi<PrincipalBo>, ExposedPersistenceApi {

    override fun onModuleLoad() {
        super.onModuleLoad()
        + PrincipalExposedTable
    }

    override fun <R> withTransaction(func: () -> R) = transaction {
        func()
    }

    override fun commit() = exposedCommit()

    override fun rollback() = exposedRollback()

    override fun list(): List<PrincipalBo> {
        return PrincipalExposedTable
            .selectAll()
            .map { it.toBo() }
    }

    override fun create(bo: PrincipalBo): PrincipalBo {
        PrincipalExposedTable
            .insertAndGetId { it.fromBo(bo) }
            .also { bo.id = EntityId(it.value) }
        return bo
    }

    override fun read(entityId: EntityId<PrincipalBo>) : PrincipalBo {
        return PrincipalExposedTable
            .select { PrincipalExposedTable.id eq entityId.toLong() }
            .first()
            .toBo()
    }

    override fun update(bo: PrincipalBo): PrincipalBo {
        PrincipalExposedTable
            .update({ PrincipalExposedTable.id eq bo.id.toLong() }) { it.fromBo(bo) }
        return bo
    }

    override fun delete(entityId: EntityId<PrincipalBo>) {
        PrincipalExposedTable
            .deleteWhere { PrincipalExposedTable.id eq entityId.toLong() }
    }
    
    open fun ResultRow.toBo() = PrincipalExposedTable.toBo(this)

    open fun UpdateBuilder<*>.fromBo(bo : PrincipalBo) = PrincipalExposedTable.fromBo(this, bo)

}

object PrincipalExposedTable : LongIdTable("principal_bo") {

    val validated = bool("validated")
    val locked = bool("locked")
    val expired = bool("expired")
    val credentials = varchar("credentials", 200).nullable()
    val resetKey = varchar("reset_key", 200).nullable()
    val resetKeyExpiration = timestamp("reset_key_expiration").nullable()
    val lastLoginSuccess = timestamp("last_login_success").nullable()
    val loginSuccessCount = integer("login_success_count")
    val lastLoginFail = timestamp("last_login_fail").nullable()
    val loginFailCount = integer("login_fail_count")

    fun toBo(row: ResultRow) = PrincipalBo(
        id = row[id].entityId(),
        validated = row[validated],
        locked = row[locked],
        expired = row[expired],
        credentials = null /* do not send out the secret */,
        resetKey = null /* do not send out the secret */,
        resetKeyExpiration = row[resetKeyExpiration]?.toKotlinInstant(),
        lastLoginSuccess = row[lastLoginSuccess]?.toKotlinInstant(),
        loginSuccessCount = row[loginSuccessCount],
        lastLoginFail = row[lastLoginFail]?.toKotlinInstant(),
        loginFailCount = row[loginFailCount]
    )

    fun fromBo(statement: UpdateBuilder<*>, bo: PrincipalBo) {
        statement[validated] = bo.validated
        statement[locked] = bo.locked
        statement[expired] = bo.expired
        statement[credentials] = bo.credentials?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        statement[resetKey] = bo.resetKey?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        statement[resetKeyExpiration] = bo.resetKeyExpiration?.toJavaInstant()
        statement[lastLoginSuccess] = bo.lastLoginSuccess?.toJavaInstant()
        statement[loginSuccessCount] = bo.loginSuccessCount
        statement[lastLoginFail] = bo.lastLoginFail?.toJavaInstant()
        statement[loginFailCount] = bo.loginFailCount
    }

}

