/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.persistenceapi

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.schedule.data.Subscription
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid

/**
 * Exposed based Persistence API for Subscription.
 * 
 * Generated with Bender at 2021-07-06T16:20:36.421Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class SubscriptionExposedPaGen : ExposedPaBase<Subscription,SubscriptionExposedTableGen>(
    table = SubscriptionExposedTableGen
) {
    override fun ResultRow.toBo() = Subscription(
        id = this[table.id].entityId(),
        nodeAddress = this[table.nodeAddress],
        nodeId = this[table.nodeId].toStackUuid(),
        actionNamespace = this[table.actionNamespace],
        actionType = this[table.actionType]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: Subscription) {
        this[table.nodeAddress] = bo.nodeAddress
        this[table.nodeId] = bo.nodeId.toJavaUuid()
        this[table.actionNamespace] = bo.actionNamespace
        this[table.actionType] = bo.actionType
    }
}

/**
 * Exposed based SQL table for Subscription.
 * 
 * Generated with Bender at 2021-07-06T16:20:36.421Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object SubscriptionExposedTableGen : ExposedPaTable<Subscription>(
    tableName = "subscription"
) {

    internal val nodeAddress = text("node_address")
    internal val nodeId = uuid("node_id")
    internal val actionNamespace = text("action_namespace").nullable()
    internal val actionType = text("action_type").nullable()

}