/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class KtorSettingsDto(

    var port: Int = 8080,
    var websocket: WebSocketSettingsDto = WebSocketSettingsDto()

) : DtoBase {

    override fun schema() = DtoSchema {
        + ::port default 8080
        + ::websocket
    }

}