/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class DatabaseSettingsDto(

    var driverClassName: String,
    var jdbcUrl: String,
    var username: String,
    var password: String // FIXME replace this when YAML loader knows how to

) : DtoBase {

    override fun schema() = DtoSchema {
        + ::driverClassName
        + ::jdbcUrl
        + ::username
        + ::password
    }

}