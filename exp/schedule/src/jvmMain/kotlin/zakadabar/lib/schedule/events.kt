/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.datetime.Instant
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.UUID

interface DispatcherEvent {
    val actionNamespace: String?
    val actionType: String?
}

interface JobEvent : DispatcherEvent {
    override val actionNamespace: String
    override val actionType: String
    val jobId: EntityId<Job>
    val specific: Boolean
}

open class JobCreateEvent(
    override val actionNamespace: String,
    override val actionType: String,
    override val jobId: EntityId<Job>,
    override val specific: Boolean,
    val actionData : String,
    val startAt: Instant?
) : JobEvent

open class JobSuccessEvent(
    override val actionNamespace: String,
    override val actionType: String,
    override val jobId: EntityId<Job>,
    override val specific: Boolean,
) : JobEvent

open class JobFailEvent(
    override val actionNamespace: String,
    override val actionType: String,
    override val jobId: EntityId<Job>,
    override val specific: Boolean,
    val retryAt: Instant?,
    val actionData : String
) : JobEvent

open class JobCancelEvent(
    override val actionNamespace: String,
    override val actionType: String,
    override val jobId: EntityId<Job>,
    override val specific: Boolean
) : JobEvent

open class RequestJobCancelEvent(
    override val actionNamespace: String,
    override val actionType: String,
    override val jobId: EntityId<Job>,
    override val specific: Boolean
) : JobEvent

open class SubscriptionCreateEvent(
    override val actionNamespace: String?,
    override val actionType: String?,
    var subscriptionId: EntityId<Subscription>,
    var nodeUrl: String,
    var nodeId: UUID,
) : DispatcherEvent

open class SubscriptionDeleteEvent(
    override val actionNamespace: String?,
    override val actionType: String?,
    var subscriptionId: EntityId<Subscription>
) : DispatcherEvent

open class PushFailEvent(
    override val actionNamespace: String,
    override val actionType: String,
    override val jobId: EntityId<Job>,
    override val specific: Boolean
) : JobEvent

open class PendingCheckEvent(
    override val actionNamespace: String? = null,
    override val actionType: String? = null
) : DispatcherEvent
