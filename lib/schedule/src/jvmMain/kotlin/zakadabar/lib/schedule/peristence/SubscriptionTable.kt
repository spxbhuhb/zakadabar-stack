/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.peristence

import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.lib.schedule.data.Subscription

class SubscriptionTable : ExposedPaTable<Subscription>(
    tableName = "schedule_subscription"
) {

    val nodeId = uuid("node_id")
    val nodeBaseUrl = text("node_base_url").nullable()
    val nodeNamespace = text("node_namespace").nullable()
    val nodeFullUrl = text("node_full_url").nullable()
    val nodeLocal = bool("node_local").default(false)
    val actionNamespace = text("action_namespace").nullable()
    val actionType = text("action_type").nullable()

}