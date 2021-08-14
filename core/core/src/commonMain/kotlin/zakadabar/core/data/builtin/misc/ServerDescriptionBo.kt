/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.core.data.builtin.misc

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.schema.BoSchema

@Serializable
class ServerDescriptionBo(

    var name: String,
    var version: String,
    var defaultLocale : String,

) : BaseBo {

    override fun schema() = BoSchema {
        + ::name min 1 max 100 blank false
        + ::version min 1 max 20 blank false
        + ::defaultLocale min 2 max 5 blank false
    }

}