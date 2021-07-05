/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

@Serializable
class ServerSettingsBo(

    var settingsDirectory : String = "",
    var serverName: String = "",
    var defaultLocale: String = "en",
    var database: DatabaseSettingsBo,
    var traceRouting: Boolean = false,
    var staticResources: String = "./var/static",
    var apiCacheControl : String = "no-cache, no-store",
    var ktor: KtorSettingsBo = KtorSettingsBo(),
    var modules: List<String> = emptyList()

) : BaseBo {

    override fun schema() = BoSchema {
        + ::settingsDirectory default settingsDirectory
        + ::serverName default serverName
        + ::defaultLocale default defaultLocale
        + ::database
        + ::traceRouting default traceRouting
        + ::staticResources default staticResources
        + ::apiCacheControl default apiCacheControl
        + ::ktor
        // + ::modules
    }

}
