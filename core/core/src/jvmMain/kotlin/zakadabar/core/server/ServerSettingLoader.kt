/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import com.charleskorn.kaml.Yaml
import zakadabar.core.setting.JvmSystemEnvHandler
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Bootstrap settings loader which runs before anything else.
 *
 * @param  useEnvAuto  When true, environment variables are merged into setting BOs with automatic naming.
 * @param  useEnvExplicit  When true, environment variables are merged into setting BOs with explicit naming.
 */
open class ServerSettingLoader(
    val useEnvAuto: Boolean = false,
    val useEnvExplicit: Boolean = false
) : JvmSystemEnvHandler {

    open fun load(settingsPath: String): ServerSettingsBo {

        val paths = listOf(
            settingsPath,
            "./stack.server.yml",
            "./etc/stack.server.yaml",
            "./etc/stack.server.yml",
            "../etc/stack.server.yaml",
            "../etc/stack.server.yml",
            "./template/app/etc/stack.server.yaml" // this is for development
        )

        for (p in paths) {
            val path = Paths.get(p)
            if (! Files.exists(path)) continue

            val source = Files.readAllBytes(path).decodeToString()

            settingsLogger.info("ServerSettingsBo source: ${path.toAbsolutePath()}")

            val bo = Yaml.default.decodeFromString(ServerSettingsBo.serializer(), source)
            bo.settingsDirectory = path.parent.toAbsolutePath().toString()

            if (useEnvAuto || useEnvExplicit) {
                val env = System.getenv()
                if (useEnvAuto) mergeEnvironmentAuto(bo, "stack.server", env)
                if (useEnvExplicit) mergeEnvironmentExplicit(bo, env)
            }

            return bo
        }

        throw IllegalArgumentException("cannot locate server settings file")
    }


}