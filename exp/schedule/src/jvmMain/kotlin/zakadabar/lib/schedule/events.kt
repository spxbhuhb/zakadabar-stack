/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.UUID

interface DispatcherEvent

open class JobCreateEvent(
   partitionJobEntry: PartitionJobEntry
) : DispatcherEvent

open class JobStatusUpdateEvent(
    open val jobId: EntityId<Job>,
    open val status: JobStatus
) : DispatcherEvent

open class SubscriptionCreateEvent(
    open var subscriptionId: EntityId<Subscription>,
    open var nodeUrl: String,
    open var nodeId : UUID,
    open val actionNamespace: String?,
    open val actionType: String?
) : DispatcherEvent

open class SubscriptionDeleteEvent(
    open var subscriptionId: EntityId<Subscription>
) : DispatcherEvent

