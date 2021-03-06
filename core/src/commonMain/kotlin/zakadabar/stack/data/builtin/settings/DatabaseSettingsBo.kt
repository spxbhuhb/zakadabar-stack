/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

@Serializable
data class DatabaseSettingsBo(

    var driverClassName: String,
    var jdbcUrl: String,
    var username: String,
    var password: String, // FIXME replace this when YAML loader knows how to
    var debugSql: Boolean = false

) : BaseBo {

    override fun schema() = BoSchema {
        + ::driverClassName
        + ::jdbcUrl
        + ::username
        + ::password
        + ::debugSql
    }

}