/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import zakadabar.core.data.BaseBo
import zakadabar.core.schema.entries.BaseBoBoSchemaEntry

/**
 * Provides functions to related to environment variables and settings.
 */
interface JvmSystemEnvHandler {

    /**
     * Merges environment variables into a BO. If the variable does not exist,
     * the appropriate BO field remains unchanged.
     *
     * Environment variable name construction:
     *
     * 1. uppercase `namespace` and the `propertyName`
     * 1. replace `.` with `_` in the `namespace`
     * 1. if namespace is not empty: `<namespace>_<propertyName>` else `propertyName`
     *
     * Example:
     *
     * ```text
     * namespace = settings.test
     * property = fromEnv
     * environment name = "SETTINGS_TEST_FROMENV
     * ```
     *
     * For nested BOs the mapping concatenates the names:
     *
     * ```text
     * namespace = settings.test
     * property = nested.fromEnv
     * environment name = "SETTINGS_TEST_NESTED_FROMENV
     * ```
     *
     * Environment variable merge **does not support** override of:
     *
     * - lists
     */
    fun mergeEnvironmentAuto(bo: BaseBo, namespace: String, env: Map<String, String>) {
        val uNamespace = namespace.uppercase().replace('.', '_')

        bo.schema().entries.forEach { (prop, schemaEntry) ->
            val propName = prop.name.uppercase()

            if (schemaEntry !is BaseBoBoSchemaEntry<*>) {
                val envName = if (uNamespace.isNotEmpty()) uNamespace + "_" + propName else propName
                env[envName]?.let {
                    schemaEntry.setFromText(it)
                }
            } else {
                mergeEnvironmentAuto(schemaEntry.kProperty.get(), "$namespace.$propName", env)
            }
        }
    }

    /**
     * Merge environment variables into the BO based on explicit environment variable
     * pairing (set by the [envVar] function).
     */
    fun mergeEnvironmentExplicit(bo: BaseBo, env: Map<String, String>) {
        val l = bo.schema().extensions(EnvVarBoSchemaEntryExtension::class)

            l.forEach {
                val e = env[it.second.name]
           e?.let { ev ->
                it.first.setFromText(ev)
            }
        }
    }
}