/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.data.RecordBackend
import zakadabar.stack.backend.util.AuthorizationException
import zakadabar.stack.data.builtin.SystemDto
import zakadabar.stack.data.builtin.security.CommonAccountDto
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.util.Executor

object BackendContext {

    val system by lazy {
        transaction {
            EntityDao.find { EntityTable.parent.isNull() and (EntityTable.type eq SystemDto.recordType) }.firstOrNull() !!
        }
    }

    val anonymous by lazy {
        transaction {
            // FIXME CommonAccountDto.type should be swappable or maybe we should use "anonymous" directly
            EntityDao.find { (EntityTable.name eq "anonymous") and (EntityTable.type eq CommonAccountDto.recordType) }
                .firstOrNull() !!
        }
    }

    /**
     * When true GET (read and query) requests are logged by DTO backends.
     */
    var logReads: Boolean = true

    val dtoBackends = mutableListOf<RecordBackend<*>>()

    operator fun plusAssign(dtoBackend: RecordBackend<*>) {
        this.dtoBackends += dtoBackend
        dtoBackend.init()
    }

    /**
     * Require write access for the given executor on the given entity.
     *
     * @throws  AuthorizationException
     */
    fun requireWriteFor(executor: Executor, entityId: Long?) {
        // FIXME implement auth check
    }

    /**
     * Checks if the given principal has read access according to the given
     * security domain.
     */
    fun hasReadAccess(executor: Executor, entityStatus: EntityStatus, aclId: Long?): Boolean {
        // FIXME implement auth check
        return true
    }

    /**
     * Require that the executor has the given role.
     *
     * @throws  AuthorizationException
     */
    fun requireRole(executor: Executor, rolePath: String) {
        // FIXME implement role check
    }

}