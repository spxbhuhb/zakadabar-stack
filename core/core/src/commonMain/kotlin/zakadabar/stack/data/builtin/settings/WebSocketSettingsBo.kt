/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.schema.BoSchema

@Serializable
data class WebSocketSettingsBo(

    var pingPeriod: Long = 60,
    var timeout: Long = 15,
    var maxFrameSize: Long = 1000000,
    var masking: Boolean = false

) : BaseBo {

    override fun schema() = BoSchema {
        + ::pingPeriod min 0 default pingPeriod
        + ::timeout min 0 default timeout
        + ::maxFrameSize min 0 default maxFrameSize
        + ::masking default masking
    }

}
