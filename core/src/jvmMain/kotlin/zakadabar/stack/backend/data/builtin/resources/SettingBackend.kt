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
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.resources.SettingBo
import zakadabar.stack.data.builtin.resources.SettingSource
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.descriptor.BoDescriptor
import kotlin.reflect.KClass

object SettingBackend : EntityBackend<SettingBo>() {

    override val boClass = SettingBo::class

    private val buildMutex = Mutex()

    private val instances = mutableMapOf<Pair<String, KClass<out BaseBo>>, Pair<SettingBo, BaseBo>>()

    override fun onModuleLoad() {
        Sql.tables +=  SettingTable
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
//                 SettingBo(
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

//    override fun create(executor: Executor, bo: SettingBo) = transaction {
//
//        authorize(executor, StackRoles.siteAdmin)
//
//        SettingDao.new {
//            namespace = bo.namespace
//            className = bo.className
//            descriptor = bo.descriptor
//        }.toBo()
//    }

    override fun read(executor: Executor, entityId: EntityId<SettingBo>) = transaction {

        authorize(true) // authorize for all users, throw not found if role is missing

        val dao = SettingDao[entityId]

        dao.role?.id?.let { if (! executor.hasRole(EntityId(it.value))) throw NotFoundException() }

        dao.toBo()
    }

//    override fun update(executor: Executor, bo: SettingBo) = transaction {
//
//        authorize(executor, StackRoles.siteAdmin)
//
//        val dao = SettingDao[bo.id]
//        with(dao) {
//            namespace = bo.namespace
//            className = bo.className
//            descriptor = bo.descriptor
//        }
//        dao.toBo()
//    }

//    override fun delete(executor: Executor, entityId:  EntityId<SettingBo>) = transaction {
//
//        authorize(executor, StackRoles.siteAdmin)
//
//        SettingDao[entityId].delete()
//    }


    @Suppress("UNCHECKED_CAST") // key contains class
    fun <T : BaseBo> get(default: T, namespace: String, serializer: KSerializer<T>) = runBlocking {

        val key = namespace to default::class

        buildMutex.withLock {

            val entry = instances[key]
            var instance = entry?.second as? T

            if (instance != null) return@runBlocking instance

            val bo = transaction {
                SettingDao
                    .find { (SettingTable.namespace eq namespace) and (SettingTable.className eq default::class.simpleName !!) }
                    .firstOrNull()
                    ?.toBo()
            }

            if (bo != null) {
                val descriptor = Json.decodeFromString(BoDescriptor.serializer(), bo.descriptor !!)
                instance = default
                instance.schema().push(descriptor)
                instances[key] = bo to instance
                return@runBlocking instance
            }

            instance = Server.loadSettings(namespace, serializer)

            if (instance != null) {
                instances[key] = buildBo(SettingSource.File, instance, namespace, serializer) to instance
                return@runBlocking instance
            }

            instance = default
            instances[key] = buildBo(SettingSource.Default, instance, namespace, serializer) to instance
            return@runBlocking instance
        }

    }

    private fun <T : BaseBo> buildBo(source: SettingSource, instance: T, namespace: String, serializer: KSerializer<T>) =
        SettingBo(
            id = EntityId(),
            role = null, //RoleBackend.findForName(StackRoles.securityOfficer),
            source = SettingSource.Default,
            namespace = namespace,
            className = instance::class.simpleName !!,
            descriptor = Json.encodeToString(BoDescriptor.serializer(), instance.schema().toBoDescriptor())
        )
}