/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.peristence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.schedule.api.Subscription
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid

open class SubscriptionPa : ExposedPaBase<Subscription, SubscriptionTable>(
    table = SubscriptionTable()
) {
    override fun ResultRow.toBo() = Subscription(
        id = this[table.id].entityId(),
        nodeUrl = this[table.nodeUrl],
        nodeId = this[table.nodeId].toStackUuid(),
        actionNamespace = this[table.actionNamespace],
        actionType = this[table.actionType]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: Subscription) {
        this[table.nodeUrl] = bo.nodeUrl
        this[table.nodeId] = bo.nodeId.toJavaUuid()
        this[table.actionNamespace] = bo.actionNamespace
        this[table.actionType] = bo.actionType
    }
}