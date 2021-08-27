/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema

@Serializable
class SettingTestBo(
    var fromFile: String,
    var fromEnv: String? = null
) : BaseBo {

    override fun schema() = BoSchema {
        + ::fromFile
        + ::fromEnv
    }

}