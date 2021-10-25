/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.entityId
import zakadabar.lib.content.data.StatusBo

open class StatusPa : ExposedPaBase<StatusBo,StatusTable>(
    table = StatusTable
) {
    override fun ResultRow.toBo() = StatusBo(
        id = this[table.id].entityId(),
        name = this[table.name]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: StatusBo) {
        this[table.name] = bo.name
    }
}

