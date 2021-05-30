/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.backend.setting

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.backend.authorize.EmptyAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.backend.route.EmptyRouter
import zakadabar.stack.backend.server
import zakadabar.stack.backend.settingsLogger
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.resources.SettingBo
import zakadabar.stack.data.builtin.resources.SettingSource
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.descriptor.BoDescriptor
import java.nio.file.Files
import kotlin.collections.set
import kotlin.reflect.KClass

/**
 * BL for setting management.
 */
class SettingBl : EntityBusinessLogicBase<SettingBo>(
    boClass = SettingBo::class
), SettingProvider {

    override val pa = EmptyPersistenceApi<SettingBo>()

    override val authorizer = EmptyAuthorizer<SettingBo>()

    override val router = EmptyRouter<SettingBo>()

    private val buildMutex = Mutex()

    private val instances = mutableMapOf<Pair<String, KClass<out BaseBo>>, Pair<SettingBo, BaseBo>>()

    @Suppress("UNCHECKED_CAST") // key contains class
    override fun <T : BaseBo> get(default: T, namespace: String, serializer: KSerializer<T>) = runBlocking {

        val key = namespace to default::class

        buildMutex.withLock {

            val entry = instances[key]
            var instance = entry?.second as? T

            if (instance != null) return@runBlocking instance

            // this is an old code that loaded settings from SQL, as of know this is not
            // supported because we don't have the necessary form building capabilities

//            val bo = transaction {
//                SettingDao
//                    .find { (SettingTable.namespace eq namespace) and (SettingTable.className eq default::class.simpleName !!) }
//                    .firstOrNull()
//                    ?.toBo()
//            }
//
//            if (bo != null) {
//                val descriptor = Json.decodeFromString(BoDescriptor.serializer(), bo.descriptor !!)
//                instance = default
//                instance.schema().push(descriptor)
//                instances[key] = bo to instance
//                return@runBlocking instance
//            }

            instance = loadFile(namespace, serializer)

            if (instance != null) {
                instance.schema().validate()
                instances[key] = buildBo(SettingSource.File, instance, namespace) to instance
                return@runBlocking instance
            }

            instance = default
            instance.schema().validate()
            instances[key] = buildBo(SettingSource.Default, instance, namespace) to instance
            return@runBlocking instance
        }

    }

    private fun <T : BaseBo> loadFile(namespace: String, serializer: KSerializer<T>): T? {

        val p1 = server.settingsDirectory.resolve("$namespace.yaml")
        val p2 = server.settingsDirectory.resolve("$namespace.yml")

        val path = when {
            Files.isReadable(p1) -> p1
            Files.isReadable(p2) -> p2
            else -> null
        }

        if (path == null) {
            val tried = server.settingsDirectory.resolve(namespace).toString() + "[.yaml,.yml]"
            settingsLogger.info("no file for namespace $namespace (tried: $tried)")
            return null
        }

        val source = Files.readAllBytes(path).decodeToString()

        return try {
            val sObj = Yaml.default.decodeFromString(serializer, source)
            settingsLogger.info("${sObj::class.simpleName} source: ${path.toAbsolutePath()}")
            sObj
        } catch (ex : Exception) {
            settingsLogger.error("could not load ${path.toAbsolutePath()}")
            throw ex
        }
    }

    private fun <T : BaseBo> buildBo(source: SettingSource, instance: T, namespace: String) =
        SettingBo(
            id = EntityId(),
            source = source,
            namespace = namespace,
            className = instance::class.simpleName !!,
            descriptor = Json.encodeToString(BoDescriptor.serializer(), instance.schema().toBoDescriptor())
        )


}