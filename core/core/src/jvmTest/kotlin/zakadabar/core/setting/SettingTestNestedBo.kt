/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema

@Serializable
class SettingTestNestedBo(
    var fromFile: String = "",
    var fromEnvAuto: String? = null,
    var fromEnvExplicit : String? = null
) : BaseBo {

    override fun schema() = BoSchema {
        + ::fromEnvAuto
        + ::fromEnvExplicit envVar "NESTED_FROM_ENV_EXP"
    }

}