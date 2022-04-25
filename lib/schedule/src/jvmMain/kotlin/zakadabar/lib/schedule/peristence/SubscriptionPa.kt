/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.peristence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.comm.CommConfig
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.util.UUID
import zakadabar.core.util.toJavaUuid
import zakadabar.core.util.toStackUuid
import zakadabar.lib.schedule.data.Subscription

open class SubscriptionPa : ExposedPaBase<Subscription, SubscriptionTable>(
    table = SubscriptionTable()
) {
    override fun ResultRow.toBo() = Subscription(
        id = this[table.id].entityId(),
        nodeId = this[table.nodeId].toStackUuid(),
        nodeComm = CommConfig(
            baseUrl = this[table.nodeBaseUrl],
            namespace = this[table.nodeNamespace],
            fullUrl = this[table.nodeFullUrl],
            local = this[table.nodeLocal]
        ),
        actionNamespace = this[table.actionNamespace],
        actionType = this[table.actionType]
    )

    override fun UpdateBuilder<*>.fromBo(bo: Subscription) {
        this[table.nodeId] = bo.nodeId.toJavaUuid()
        this[table.nodeBaseUrl] = bo.nodeComm.baseUrl
        this[table.nodeNamespace] = bo.nodeComm.namespace
        this[table.nodeLocal] = bo.nodeComm.local
        this[table.actionNamespace] = bo.actionNamespace
        this[table.actionType] = bo.actionType
    }

    fun delete(uuid : UUID) =
        table
            .deleteWhere { table.nodeId eq uuid.toJavaUuid() }

}