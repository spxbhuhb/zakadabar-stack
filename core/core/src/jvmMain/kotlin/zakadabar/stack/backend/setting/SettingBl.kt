/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.backend.setting

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.backend.RoutedModule
import zakadabar.core.backend.authorize.EmptyAuthorizer
import zakadabar.core.backend.business.EntityBusinessLogicBase
import zakadabar.core.backend.persistence.EmptyPersistenceApi
import zakadabar.core.backend.route.EmptyRouter
import zakadabar.core.backend.server
import zakadabar.core.backend.settingsLogger
import zakadabar.core.data.BaseBo
import zakadabar.core.data.builtin.resources.SettingBo
import zakadabar.core.data.builtin.resources.SettingSource
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.schema.descriptor.BoDescriptor
import zakadabar.core.setting.SettingProvider
import java.nio.file.Files
import kotlin.collections.set
import kotlin.reflect.KClass

/**
 * BL for setting management.
 *
 * @param  useEnv  When true, environment variables are merged into setting BOs.
 */
open class SettingBl(
    val useEnv : Boolean = true
) : EntityBusinessLogicBase<SettingBo>(
    boClass = SettingBo::class,
), SettingProvider, RoutedModule, JvmSystemEnvHandler {

    override val pa = EmptyPersistenceApi<SettingBo>()

    override val authorizer = EmptyAuthorizer<SettingBo>()

    override val router = EmptyRouter<SettingBo>()

    open val buildMutex = Mutex()

    open val instances = mutableMapOf<Pair<String, KClass<out BaseBo>>, Pair<SettingBo, BaseBo>>()

    @Suppress("UNCHECKED_CAST") // key contains class
    override fun <T : BaseBo> get(default: T, namespace: String, serializer: KSerializer<T>) = runBlocking {

        val key = namespace to default::class

        buildMutex.withLock {

            instances[key]?.let { return@runBlocking it.second as T }

            val fromFile = loadFile(namespace, serializer)

            val instance: T
            val source: SettingSource

            if (fromFile == null) {
                instance = default
                source = SettingSource.Default
            } else {
                instance = fromFile
                source = SettingSource.File
            }

            if (useEnv) {
                mergeEnvironment(instance, namespace, System.getenv())
            }

            instance.schema().validate()

            instances[key] = buildBo(source, instance, namespace) to instance

            return@runBlocking instance
        }

    }

    open fun <T : BaseBo> loadFile(namespace: String, serializer: KSerializer<T>): T? {

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
        } catch (ex: Exception) {
            settingsLogger.error("could not load ${path.toAbsolutePath()}")
            throw ex
        }
    }

    open fun <T : BaseBo> buildBo(source: SettingSource, instance: T, namespace: String) = SettingBo(
        id = EntityId(),
        source = source,
        namespace = namespace,
        className = instance::class.simpleName !!,
        descriptor = Json.encodeToString(BoDescriptor.serializer(), instance.schema().toBoDescriptor())
    )

}