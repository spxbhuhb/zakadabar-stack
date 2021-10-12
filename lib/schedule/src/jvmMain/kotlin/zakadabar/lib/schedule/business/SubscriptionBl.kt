/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.EntityId
import zakadabar.core.module.module
import zakadabar.lib.schedule.data.Subscription
import zakadabar.lib.schedule.peristence.SubscriptionPa

open class SubscriptionBl : EntityBusinessLogicBase<Subscription>(
    boClass = Subscription::class
) {

    override val pa = SubscriptionPa()

    override val authorizer by provider()

    open val jobBl by module<JobBl>()

    override fun create(executor: Executor, bo: Subscription): Subscription =
        pa
            .create(bo)
            .also {
                SubscriptionCreateEvent(
                    actionNamespace = it.actionNamespace,
                    actionType = it.actionType,
                    subscriptionId = it.id,
                    nodeUrl = it.nodeUrl,
                    nodeId = it.nodeId
                ).dispatch()
            }


    override fun update(executor: Executor, bo: Subscription): Subscription {
        throw NotImplementedError("subscriptions cannot be updated, delete and create a new")
    }

    override fun delete(executor: Executor, entityId: EntityId<Subscription>) {

        val bo = pa.readOrNull(entityId) ?: return

        pa.delete(entityId)

        SubscriptionDeleteEvent(
            bo.actionNamespace,
            bo.actionType,
            bo.id
        ).dispatch()
    }

    fun DispatcherEvent.dispatch() = jobBl.dispatchEvent(this)

}