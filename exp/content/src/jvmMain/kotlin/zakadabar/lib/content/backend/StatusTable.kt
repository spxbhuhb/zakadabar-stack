/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.lib.content.data.StatusBo

object StatusTable : ExposedPaTable<StatusBo>(
    tableName = "content_status"
) {

    val name = varchar("name", 100)

}