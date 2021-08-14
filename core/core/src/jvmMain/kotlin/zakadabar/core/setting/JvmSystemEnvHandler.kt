/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import zakadabar.core.data.BaseBo

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
     * Environment variable merge **does not support** override of:
     *
     * - nested instances
     * - lists
     */
    fun mergeEnvironment(bo: BaseBo, namespace: String, env: Map<String, String>) {
        val ns = namespace.uppercase().replace('.', '_')

        bo.schema().entries.forEach { (prop, value) ->
            val upn = prop.name.uppercase()
            val key = if (ns.isNotEmpty()) ns + "_" + upn else upn
            env[key]?.let {
                value.setFromText(it)
            }
        }
    }

}