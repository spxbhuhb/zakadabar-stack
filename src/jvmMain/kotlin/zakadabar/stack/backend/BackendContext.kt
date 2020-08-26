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

package zakadabar.stack.backend

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.extend.BackendModule
import zakadabar.stack.backend.util.AuthorizationException
import zakadabar.stack.data.SystemDto
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.util.CatalogOfUniques
import zakadabar.stack.util.Executor

object BackendContext {

    val system by lazy {
        transaction {
            EntityDao.find { (EntityTable.name eq "system") and (EntityTable.type eq SystemDto.type) }.firstOrNull() !!
        }
    }

    val anonymous by lazy {
        transaction {
            // FIXME CommonAccountDto.type should be swappable or maybe we should use "anonymous" directly
            EntityDao.find { (EntityTable.name eq "anonymous") and (EntityTable.type eq CommonAccountDto.type) }
                .firstOrNull() !!
        }
    }

    val modules = CatalogOfUniques<BackendModule>()

    operator fun plusAssign(module: BackendModule) {
        this.modules += module
    }

    /**
     * Require write access on the entity for the given principal.
     *
     * @param   entityId  Id of the entity to request write access on. When it is
     *                    null, the request is for creating a top-level entity without
     *                    a parent.
     *
     * @return  security domain the entity belongs to
     *
     * @throws  AuthorizationException
     */
    fun requireWriteFor(executor: Executor, entityId: Long?): Long {
        return 1L
    }

    /**
     * Checks if the given principal has read access according to the given
     * security domain.
     */
    fun hasReadAccess(executor: Executor, entityStatus: EntityStatus, aclId: Long?): Boolean {
        return true
    }

}