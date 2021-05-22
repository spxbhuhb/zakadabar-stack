/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

@Serializable
class ServerSettingsDto(

    var serverName: String = "",
    var database: DatabaseSettingsDto,
    var traceRouting: Boolean = false,
    var staticResources: String = "./var/static",
    var apiCacheControl : String = "no-cache, no-store",
    var ktor: KtorSettingsDto = KtorSettingsDto(),
    var modules: List<String> = emptyList()

) : BaseBo {

    override fun schema() = BoSchema {
        + ::serverName default serverName
        + ::database
        + ::traceRouting default traceRouting
        + ::staticResources default staticResources
        + ::apiCacheControl default apiCacheControl
        + ::ktor
        // + ::modules
    }

}
