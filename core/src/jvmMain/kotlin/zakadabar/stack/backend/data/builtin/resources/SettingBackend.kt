/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.resources

import io.ktor.features.*
import io.ktor.routing.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.builtin.role.RoleBackend
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.data.builtin.resources.SettingSource
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass

object SettingBackend : RecordBackend<SettingDto>() {

    override val dtoClass = SettingDto::class

    private val buildMutex = Mutex()

    private val instances = mutableMapOf<Pair<String, KClass<out DtoBase>>, Pair<SettingDto, DtoBase>>()

    override fun onModuleLoad() {
        + SettingTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(true) // authorized for all users, filter settings by roles later

        val roleIds = executor.roleIds.map { EntityID(it, RoleTable) } + null

        SettingTable
            .select { SettingTable.role inList roleIds }
            .map(SettingTable::toDto)
    }

    override fun create(executor: Executor, dto: SettingDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        SettingDao.new {
            namespace = dto.namespace
            className = dto.className
            value = dto.value
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {

        authorize(true) // authorize for all users, throw not found if role is missing

        val dao = SettingDao[recordId]

        dao.role?.id?.let { if (! executor.hasRole(it.value)) throw NotFoundException() }

        dao.toDto()
    }

    override fun update(executor: Executor, dto: SettingDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = SettingDao[dto.id]
        with(dao) {
            namespace = dto.namespace
            className = dto.className
            value = dto.value
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        SettingDao[recordId].delete()
    }


    @Suppress("UNCHECKED_CAST") // key contains class
    fun <T : DtoBase> get(default: T, namespace: String, serializer: KSerializer<T>) = runBlocking {

        val key = namespace to default::class

        buildMutex.withLock {

            val entry = instances[key]
            var instance = entry?.second as? T

            if (instance != null) return@runBlocking instance

            val dto = transaction {
                SettingDao
                    .find { (SettingTable.namespace eq namespace) and (SettingTable.className eq default::class.simpleName !!) }
                    .firstOrNull()
                    ?.toDto()
            }

            if (dto != null) {
                instance = Json.decodeFromString(serializer, dto.value)
                instances[key] = dto to instance
                return@runBlocking instance
            }

            instance = Server.loadSettings(namespace, serializer)

            if (instance != null) {
                instances[key] = buildDto(SettingSource.File, instance, serializer) to instance
                return@runBlocking instance
            }

            instance = default
            instances[key] = buildDto(SettingSource.Default, instance, serializer) to instance
            return@runBlocking instance
        }

    }

    private fun <T : DtoBase> buildDto(source: SettingSource, instance: T, serializer: KSerializer<T>) =
        SettingDto(
            id = 0L,
            role = RoleBackend.findForName(StackRoles.securityOfficer),
            source = SettingSource.Default,
            namespace = namespace,
            className = instance::class.simpleName !!,
            value = Json.encodeToString(serializer, instance)
        )
}