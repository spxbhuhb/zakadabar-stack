/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.coroutines.channels.trySendBlocking
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.module.module

open class SubscriptionBl : EntityBusinessLogicBase<Subscription>(
    boClass = Subscription::class
) {

    override val pa = SubscriptionPa()

    override val authorizer by provider()

    open val dispatcher by module<Dispatcher>()

    override fun create(executor: Executor, bo: Subscription): Subscription {
        return super.create(executor, bo).also {
            dispatcher.events.trySendBlocking(
                SubscriptionCreateEvent(
                    subscriptionId = it.id,
                    nodeUrl = it.nodeUrl,
                    nodeId = it.nodeId,
                    actionNamespace = it.actionNamespace,
                    actionType = it.actionType
                )
            ).getOrThrow() // abort create if can't send to dispatcher
        }
    }

    override fun update(executor: Executor, bo: Subscription): Subscription {
        throw NotImplementedError("subscriptions cannot be updated, delete and create a new")
    }

    override fun delete(executor: Executor, entityId: EntityId<Subscription>) {
        super.delete(executor, entityId)
        dispatcher.events.trySendBlocking(
            SubscriptionDeleteEvent(entityId) // doesn't matter if this fails
        )
    }

}