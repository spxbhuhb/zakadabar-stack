/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema
import zakadabar.core.setting.envVar

@Serializable
class ServerSettingsBo(

    var settingsDirectory: String = "",
    var serverName: String = "",
    var defaultLocale: String = "en",
    var database: DatabaseSettingsBo,
    var traceRouting: Boolean = false,
    var staticResources: String = "./var/static",
    var apiCacheControl: String = "no-cache, no-store",
    var logReads : Boolean = true,
    var ktor: KtorSettingsBo = KtorSettingsBo(),
    var modules: List<String> = emptyList(),
    var xForwardedHeaderSupport: Boolean = true

) : BaseBo {

    override fun schema() = BoSchema {
        + ::settingsDirectory default settingsDirectory
        + ::serverName default serverName envVar "ZK_SERVER_NAME"
        + ::defaultLocale default defaultLocale envVar "ZK_SERVER_LOCALE"
        + ::database
        + ::traceRouting default traceRouting
        + ::staticResources default staticResources envVar "ZK_STATIC_RESOURCES"
        + ::apiCacheControl default apiCacheControl
        + ::ktor
        + ::logReads default true
        // + ::modules
        + ::xForwardedHeaderSupport default xForwardedHeaderSupport envVar "ZK_X_FORWARDED_HEADER_SUPPORT"
    }

}
