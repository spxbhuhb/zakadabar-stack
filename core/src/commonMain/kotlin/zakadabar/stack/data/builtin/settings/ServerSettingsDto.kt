/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema

@Serializable
class ServerSettingsDto(

    var serverName: String = "",
    var database: DatabaseSettingsDto,
    var traceRouting: Boolean = false,
    var staticResources: String = "./var/static",
    var ktor: KtorSettingsDto = KtorSettingsDto(),
    var modules: List<String> = emptyList()

) : DtoBase {

    override fun schema() = DtoSchema {
        + ::serverName
        + ::database
        + ::traceRouting
        + ::staticResources
        + ::ktor
        // + ::modules
    }

}
