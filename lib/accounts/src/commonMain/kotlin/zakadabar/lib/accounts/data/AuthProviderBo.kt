/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema

@Serializable
class AuthProviderBo(
    var name : String,
    var displayName : String,
    var loginPath : String,
    var svgIcon : String? = null,
) : BaseBo {

    companion object {
        const val boNamespace = "auth-provider"
    }
    override fun schema() = BoSchema {
        + ::name
        + ::displayName
        + ::svgIcon
    }

}

