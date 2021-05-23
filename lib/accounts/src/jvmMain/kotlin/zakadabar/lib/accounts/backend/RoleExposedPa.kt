/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.entity.EntityPersistenceApiBase
import zakadabar.stack.backend.data.entity.ExposedPersistenceApi
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.get
import zakadabar.stack.data.builtin.account.RoleBo
import zakadabar.stack.data.entity.EntityId

class RoleExposedPa : EntityPersistenceApiBase<RoleBo>(), ExposedPersistenceApi {

    override fun onModuleLoad() {
        super.onModuleLoad()
        + RoleExposedTable
    }

    override fun list() = RoleExposedDao.all().map { it.toBo() }

    override fun create(bo: RoleBo) = RoleExposedDao.new { fromBo(bo) }.toBo()

    override fun read(entityId: EntityId<RoleBo>) = RoleExposedDao[entityId].toBo()

    fun readByName(name: String) = RoleExposedDao.find { RoleExposedTable.name eq name }.first().toBo()

    override fun update(bo: RoleBo): RoleBo {
        RoleExposedDao[bo.id].fromBo(bo)
        return bo
    }

    override fun delete(entityId: EntityId<RoleBo>) = RoleExposedDao[entityId].delete()

}

class RoleExposedDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RoleExposedDao>(RoleExposedTable)

    var name by RoleExposedTable.name
    var description by RoleExposedTable.description

    fun toBo() = RoleBo(
        id = id.entityId(),
        name = name,
        description = description
    )

    fun fromBo(bo: RoleBo) {
        name = bo.name
        description = bo.description
    }
}

object RoleExposedTable : LongIdTable("roles") {

    val name = varchar("name", 100)
    val description = varchar("description", 4000)

    fun toBo(row: ResultRow) = RoleBo(
        id = row[id].entityId(),
        name = row[name],
        description = row[description]
    )

}