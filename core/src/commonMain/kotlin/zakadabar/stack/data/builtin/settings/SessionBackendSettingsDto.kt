/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema

@Serializable
class SessionBackendSettingsDto(

    var sessionTimeout: Long = 30000,
    var updateDelay: Long = 120,
    var expirationCheckInterval: Long = 120

) : DtoBase {

    override fun schema() = DtoSchema {
        + ::sessionTimeout default sessionTimeout
        + ::updateDelay default updateDelay
        + ::expirationCheckInterval default expirationCheckInterval
    }

}