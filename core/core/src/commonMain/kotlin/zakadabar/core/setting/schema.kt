/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import zakadabar.core.schema.BoSchemaEntry
import zakadabar.core.schema.BoSchemaEntryExtension
import zakadabar.core.util.PublicApi


/**
 * Schema builder function that sets the name of the environment variable
 * that may contain the value of this
 */
@Suppress("UNCHECKED_CAST")
@PublicApi
infix fun <T,ET : BoSchemaEntry<T,ET>> BoSchemaEntry<T,ET>.envVar(name: String): ET {
    this.extensions += EnvVarBoSchemaEntryExtension(name)
    return this as ET
}

/**
 * Schema extension that stores the environment variable to read this
 * the value of this property from.
 *
 * @param  name  Name of the environment variable.
 */
class EnvVarBoSchemaEntryExtension<T>(
    val name : String
) : BoSchemaEntryExtension<T>()