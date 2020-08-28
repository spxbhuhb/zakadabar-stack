/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.messaging

import zakadabar.stack.util.stdoutTrace
import kotlin.reflect.KClass


/**
 * A simple message dispatcher that calls all subscribers sequentially and blocks
 * execution until everything is done. Use it for short, fast, synchronized event
 * distribution.
 */
class SyncMessageDispatcher : MessageDispatcher {

    private val subscriptions = mutableMapOf<KClass<*>, Subscribers<Message>>()

    override val trace = 0

    override fun <T : Message> subscribe(messageClass: KClass<T>, receiver: (T) -> Unit) {
        // SYNC getOrPut is not atomic
        val subscribers = subscriptions.getOrPut(messageClass) { mutableListOf() }

        @Suppress("UNCHECKED_CAST") // Can't be helped. Method signature ensures type safety.
        subscribers as Subscribers<T>

        subscribers += receiver
    }

    override fun <T : Message> unsubscribe(messageType: KClass<T>, receiver: (T) -> Unit) {
        subscriptions[messageType]?.remove(receiver)
    }

    override fun <T : Message> postSync(build: () -> T) {
        val message = build()

        if (trace > 50) stdoutTrace("dispatcher.postSync", message.toString())

        val subscribers = subscriptions[message::class] ?: return // simple return when there are no subscriptions

        @Suppress("UNCHECKED_CAST") // Can't be helped. Subscribe ensures type safety.
        subscribers as Subscribers<T>

        subscribers.forEach { it(message) }
    }

    override suspend fun <T : Message> post(message: T) {
        throw NotImplementedError("sync message dispatcher cannot handle async posting")
    }

}