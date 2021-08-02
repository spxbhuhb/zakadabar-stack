/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

@Serializable
class MailSettings(
    var host : String = "",
    var port  : Int = 0,
    var username : String? = "",
    var password : String? = "",
    var protocol : String = "smtp",
    var auth : Boolean = false,
    var tls : Boolean = false,
    var debug : Boolean = false
) : BaseBo {

    override fun schema() = BoSchema {
        + ::host blank false
        + ::port min 1 max 65535
        + ::username max 100
        + ::password
        + ::protocol
        + ::auth
        + ::tls
        + ::debug
    }
}