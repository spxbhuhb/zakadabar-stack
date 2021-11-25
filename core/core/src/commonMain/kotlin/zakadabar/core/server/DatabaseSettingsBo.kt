/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema
import zakadabar.core.setting.envVar

@Serializable
data class DatabaseSettingsBo(

    var driverClassName: String,
    var jdbcUrl: String,
    var username: String,
    var password: String, // FIXME replace this when YAML loader knows how to
    var isolationLevel: String = "TRANSACTION_REPEATABLE_READ",
    var debugSql: Boolean = false

) : BaseBo {

    override fun schema() = BoSchema {
        + ::driverClassName envVar "ZK_DB_DRIVER"
        + ::jdbcUrl envVar "ZK_DB_JDBC_URL"
        + ::username envVar "ZK_DB_USERNAME"
        + ::password envVar "ZK_DB_PASSWORD"
        + ::isolationLevel envVar "ZK_DB_ISOLATION_LEVEL" default isolationLevel
        + ::debugSql envVar "ZK_DB_DEBUG" default debugSql
    }

}