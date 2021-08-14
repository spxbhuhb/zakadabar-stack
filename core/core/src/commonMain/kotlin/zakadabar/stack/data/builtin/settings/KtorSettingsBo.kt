/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.schema.BoSchema

@Serializable
data class KtorSettingsBo(

    var port: Int = 8080,
    var websocket: WebSocketSettingsBo = WebSocketSettingsBo()

) : BaseBo {

    override fun schema() = BoSchema {
        + ::port default port
        + ::websocket
    }

}