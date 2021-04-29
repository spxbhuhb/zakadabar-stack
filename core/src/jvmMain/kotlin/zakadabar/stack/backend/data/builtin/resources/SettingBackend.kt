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
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.data.builtin.resources.SettingSource
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.dto.DescriptorDto
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

//        val roleIds = executor.roleIds.map { EntityID(it, RoleTable) } + null

//        SettingTable
//            .select { SettingTable.role inList roleIds }
//            .map { row ->
//                 SettingDto(
//                    id = row[SettingTable.id].value,
//                    role = row[SettingTable.role]?.value,
//                    source = SettingSource.Database,
//                    namespace = row[SettingTable.namespace],
//                    className = row[SettingTable.className],
//                    descriptor = null // this is intentional, do not send out value in the list
//                )
//            }

        val roleIds = executor.roleIds + null

        runBlocking {
            buildMutex.withLock {
                instances.values.mapNotNull { if (it.first.role in roleIds) it.first else null }
            }
        }

    }

//    override fun create(executor: Executor, dto: SettingDto) = transaction {
//
//        authorize(executor, StackRoles.siteAdmin)
//
//        SettingDao.new {
//            namespace = dto.namespace
//            className = dto.className
//            descriptor = dto.descriptor
//        }.toDto()
//    }

    override fun read(executor: Executor, recordId: RecordId<SettingDto>) = transaction {

        authorize(true) // authorize for all users, throw not found if role is missing

        val dao = SettingDao[recordId]

        dao.role?.id?.let { if (! executor.hasRole(LongRecordId(it.value))) throw NotFoundException() }

        dao.toDto()
    }

//    override fun update(executor: Executor, dto: SettingDto) = transaction {
//
//        authorize(executor, StackRoles.siteAdmin)
//
//        val dao = SettingDao[dto.id]
//        with(dao) {
//            namespace = dto.namespace
//            className = dto.className
//            descriptor = dto.descriptor
//        }
//        dao.toDto()
//    }

//    override fun delete(executor: Executor, recordId:  RecordId<SettingDto>) = transaction {
//
//        authorize(executor, StackRoles.siteAdmin)
//
//        SettingDao[recordId].delete()
//    }


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
                val descriptor = Json.decodeFromString(DescriptorDto.serializer(), dto.descriptor !!)
                instance = default
                instance.schema().push(descriptor)
                instances[key] = dto to instance
                return@runBlocking instance
            }

            instance = Server.loadSettings(namespace, serializer)

            if (instance != null) {
                instances[key] = buildDto(SettingSource.File, instance, namespace, serializer) to instance
                return@runBlocking instance
            }

            instance = default
            instances[key] = buildDto(SettingSource.Default, instance, namespace, serializer) to instance
            return@runBlocking instance
        }

    }

    private fun <T : DtoBase> buildDto(source: SettingSource, instance: T, namespace: String, serializer: KSerializer<T>) =
        SettingDto(
            id = EmptyRecordId(),
            role = null, //RoleBackend.findForName(StackRoles.securityOfficer),
            source = SettingSource.Default,
            namespace = namespace,
            className = instance::class.simpleName !!,
            descriptor = Json.encodeToString(DescriptorDto.serializer(), instance.schema().toDescriptorDto())
        )
}