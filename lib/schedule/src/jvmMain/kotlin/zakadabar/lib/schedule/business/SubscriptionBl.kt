/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.EntityId
import zakadabar.core.module.module
import zakadabar.core.persistence.exposed.Sql.dropColumnIfExists
import zakadabar.lib.schedule.data.Subscription
import zakadabar.lib.schedule.peristence.SubscriptionPa

open class SubscriptionBl : EntityBusinessLogicBase<Subscription>(
    boClass = Subscription::class
) {

    override val pa = SubscriptionPa()

    override val authorizer by provider()

    open val jobBl by module<JobBl>()

    override fun onInitializeDb() {
        super.onInitializeDb()

        // node address is an old column before (2022.4.6)
        transaction {
            dropColumnIfExists(pa.table, "node_address")
        }

        // read all subscriptions that's been active before restart

        transaction {
            pa.list().forEach {
                SubscriptionCreateEvent(
                    actionNamespace = it.actionNamespace,
                    actionType = it.actionType,
                    subscriptionId = it.id,
                    nodeId = it.nodeId,
                    nodeComm = it.nodeComm
                ).dispatch()
            }
        }
    }

    override fun create(executor: Executor, bo: Subscription): Subscription {

        // delete the old subscription if it exists
        pa.delete(bo.nodeId)

        SubscriptionDeleteEvent(
            bo.actionNamespace,
            bo.actionType,
            bo.id
        ).dispatch()

        // create the new subscription

        val created = pa.create(bo)

        SubscriptionCreateEvent(
            actionNamespace = created.actionNamespace,
            actionType = created.actionType,
            subscriptionId = created.id,
            nodeId = created.nodeId,
            nodeComm = created.nodeComm
        ).dispatch()

        return created
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